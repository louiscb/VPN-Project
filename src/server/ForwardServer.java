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
 
import communication.handshake.Handshake;
import communication.handshake.HandshakeMessage;
import communication.handshake.VerifyCertificate;
import communication.session.SessionEncrypter;
import communication.session.SessionKey;
import meta.Arguments;
import meta.Common;
import meta.Logger;
import communication.threads.ForwardServerClientThread;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.io.IOException;

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

        try {
            handshakeSocket = new ServerSocket(port);
        } catch (IOException ioe) {
           throw new IOException("Unable to bind to port " + port);
        }

        log("Forward Server started on TCP port " + port);
 
        // Accept client connections and process them until stopped
        while(true) {
            ForwardServerClientThread forwardThread;

            try {
               doHandshake();
               setUpSession();
                //This creates communication channel between server and target
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

        /*
            Do handshake steps here
         */

        //Step 2, get ClientHello and verify

        HandshakeMessage clientHello = new HandshakeMessage();
        clientHello.recv(clientSocket);

        if (!clientHello.getParameter("MessageType").equals("ClientHello")) {
            System.out.println("Received invalid handshake type!");
            clientSocket.close();
            throw new Error();
        }

        String clientCert = clientHello.getParameter("Certificate");
        VerifyCertificate verifyCertificate = new VerifyCertificate("CaCertPath.pem", clientCert);

        if (!verifyCertificate.isUserVerified()) {
            clientSocket.close();
            throw new Error();
        }

        // Client is verified, proceed to sending ServerHello

        HandshakeMessage serverHello = new HandshakeMessage();
        serverHello.putParameter(Common.MESSAGE_TYPE, Common.SERVER_HELLO);
        serverHello.putParameter(Common.CERTIFICATE, "server certificaeteasd");
        serverHello.send(clientSocket);

        //step 6 server recieves ForwardMessage from client and sets up session
        HandshakeMessage forwardMessage = new HandshakeMessage();
        forwardMessage.recv(clientSocket);

        if (!clientHello.getParameter(Common.MESSAGE_TYPE).equals(Common.FORWARD_MSG)) {
            System.out.println("Received invalid message type! Shoud be forward message");
            clientSocket.close();
            throw new Error();
        }

        Handshake.setTargetHost(forwardMessage.getParameter(Common.TARGET_HOST));
        Handshake.setTargetPort(Integer.parseInt(forwardMessage.getParameter(Common.TARGET_PORT)));

        SessionEncrypter sessionEncrypter = new SessionEncrypter();
        sessionEncrypter.createSessionKey(Common.KEY_LENGTH);
        sessionEncrypter.createIV();

        final String secretKey = sessionEncrypter.encodeKey();
        final String iv = sessionEncrypter.encodeIV();

        // encrypt secretKey and IV with client's public key


        // create a message, caled session message
        // send to client

        clientSocket.close(); //end of handshake
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
    public void log(String aMessage)
    {
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
