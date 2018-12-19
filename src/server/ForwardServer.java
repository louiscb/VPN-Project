package server; /**
 * Port forwarding server. Forward data
 * between two TCP ports. Based on Nakov TCP Socket Forward Server 
 * and adapted for IK2206.
 *
 * Original copyright notice below.
 * (c) 2018 Peter Sjodin, KTH
 */

/**
 * Nakov TCP Socket Forward Server - freeware
 * Version 1.0 - March, 2002
 * (c) 2001 by Svetlin Nakov - http://www.nakov.com
 */
 
import communication.handshake.*;
import communication.session.IV;
import communication.session.SessionKey;
import meta.Arguments;
import meta.Common;
import meta.Logger;
import communication.threads.ForwardServerClientThread;

import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.security.cert.X509Certificate;

public class ForwardServer
{
    private static final boolean ENABLE_LOGGING = true;
    public static final int DEFAULTSERVERPORT = 2206;
    public static final String DEFAULTSERVERHOST = "localhost";
    public static final String PROGRAMNAME = "server.ForwardServer";
    private static Arguments arguments;

    private ServerSocket handshakeSocket;

    private ServerSocket listenSocket;
    private String targetHost;
    private int targetPort;

    /**
     * Program entry point. Reads settings, starts check-alive thread and
     * the forward server
     */
    public static void main(String[] args) throws Exception {
        arguments = new Arguments();
        arguments.setDefault("handshakeport", Integer.toString(DEFAULTSERVERPORT));
        arguments.setDefault("handshakehost", DEFAULTSERVERHOST);
        arguments.loadArguments(args);

        ForwardServer srv = new ForwardServer();
        try {
            srv.startForwardServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the forward server - binds on a given port and starts serving
     */
    public void startForwardServer() throws Exception {
        // Bind server on given TCP port
        int port = Integer.parseInt(arguments.get("handshakeport"));
        String address = arguments.get("handshakehost");

        try {
            handshakeSocket = new ServerSocket(port);
        } catch (IOException ioe) {
           throw new IOException("Unable to bind to port " + port);
        }

        log("Forward Server started at address " + address + " on TCP port " + port);
        log("Waiting for connections...");
 
        // Accept client connections and process them until stopped
        while(true) {
            ForwardServerClientThread forwardThread;

            try {
               doHandshake();
               setUpSession();
                //This creates communication channel between server and target
                //pass in session key and iv to forward server
               forwardThread = new ForwardServerClientThread(this.listenSocket, this.targetHost, this.targetPort);
               forwardThread.start();
           } catch (IOException e) {
               throw e;
           }
        }
    }

    /**
     * Do communication.asdf.handshake negotiation with client to authenticate, learn
     * target host/port, etc.
     */
    private void doHandshake() throws UnknownHostException, IOException, Exception {
        Socket clientSocket = handshakeSocket.accept();
        String clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        Logger.log("Incoming handshake connection from " + clientHostPort);

        //Step 2, get ClientHello and verify

        HandshakeMessage clientHello = new HandshakeMessage();
        clientHello.recv(clientSocket);

        if (!clientHello.getParameter("MessageType").equals("ClientHello")) {
            System.err.println("Received invalid handshake type!");
            clientSocket.close();
            throw new Error();
        }

        //get client certificate from message
        String clientCertString = clientHello.getParameter("Certificate");
        X509Certificate clientCert = aCertificate.stringToCert(clientCertString);

        //verify certificate is signed by our CA
        HandleCertificate handleCertificate = new HandleCertificate(arguments.get("cacert"));

        if (!handleCertificate.verify(clientCert)) {
            System.err.println("CLIENT CA FAILED VERIFICATION");
            clientSocket.close();
            throw new Error();
        } else {
            //Logger.log("Successful verification of client certificate...");
        }

        // Client is verified, proceed to sending ServerHello
        HandshakeMessage serverHello = new HandshakeMessage();
        serverHello.putParameter(Common.MESSAGE_TYPE, Common.SERVER_HELLO);
        serverHello.putParameter(Common.CERTIFICATE, aCertificate.encodeCert(aCertificate.pathToCert(arguments.get("usercert"))));
        serverHello.send(clientSocket);

        //step 6 server receives ForwardMessage from client and sets up session
        HandshakeMessage forwardMessage = new HandshakeMessage();
        forwardMessage.recv(clientSocket);

        if (!forwardMessage.getParameter(Common.MESSAGE_TYPE).equals(Common.FORWARD_MSG)) {
            System.err.println("Received invalid message type! Should be forward message");
            clientSocket.close();
            throw new Error();
        }

        //Set client's desired target as handshake target
        Handshake.setTargetHost(forwardMessage.getParameter(Common.TARGET_HOST));
        Handshake.setTargetPort(Integer.parseInt(forwardMessage.getParameter(Common.TARGET_PORT)));

        //step 6.1 generate session key and iv
        SessionKey sessionKey = new SessionKey(Common.KEY_LENGTH);
        IV iv = new IV();

        Handshake.sessionKey = sessionKey;
        Handshake.iv = iv;

        //step 6.2 encrypt secretKey and IV with client's public key
        String encryptedSessionKey = AsymmetricCrypto.encrypt(sessionKey.encodeKey(), clientCert.getPublicKey());
        String encryptedIV = AsymmetricCrypto.encrypt(iv.encodeIV(), clientCert.getPublicKey());

        //step 7 Server sends client session message
        HandshakeMessage sessionMsg = new HandshakeMessage();
        sessionMsg.putParameter(Common.MESSAGE_TYPE, Common.SESSION_MSG);
        sessionMsg.putParameter(Common.SESSION_KEY, encryptedSessionKey);
        sessionMsg.putParameter(Common.SESSION_IV, encryptedIV);

        sessionMsg.send(clientSocket);

        clientSocket.close();
        log("Handshake successful!");

        //end of handshake
    }

    private void setUpSession() throws UnknownHostException, IOException, Exception {
        /*
         * Fake the handshake result with static parameters.
         */

        /* listenSocket is a new socket where the ForwardServer waits for the
         * client to connect. The ForwardServer creates this socket and communicates
         * the socket's address to the ForwardClient during the handshake, so that the
         * ForwardClient knows to where it should connect (ServerHost/ServerPort parameters).
         * Here, we use a static address instead (serverHost/serverPort).
         * (This may give "Address already in use" errors, but that's OK for now.)
         */

        /*
            listenSocket is the session communication channel
         */
        listenSocket = new ServerSocket();
        listenSocket.bind(new InetSocketAddress(Handshake.serverHost, Handshake.serverPort));

        /* The final destination. The ForwardServer sets up port forwarding
         * between the listensocket (ie., ServerHost/ServerPort) and the target.
         */
        targetHost = Handshake.targetHost;
        targetPort = Handshake.targetPort;
    }
 
    /**
     * Prints given log message on the standart output if logging is enabled,
     * otherwise ignores it
     */
    public void log(String aMessage) {
        if (ENABLE_LOGGING)
           System.out.println(aMessage);
    }
 
    static void usage() {
        String indent = "";
        System.err.println(indent + "Usage: " + PROGRAMNAME + " options");
        System.err.println(indent + "Where options are:");
        indent += "    ";
        System.err.println(indent + "--serverhost=<hostname>");
        System.err.println(indent + "--serverport=<portnumber>");        
        System.err.println(indent + "--usercert=<filename>");
        System.err.println(indent + "--cacert=<filename>");
        System.err.println(indent + "--key=<filename>");                
    }
}