package client; /**
 * Port forwarding client. Forward data
 * between two TCP ports. Based on Nakov TCP Socket Forward Server
 * and adapted for IK2206.
 *
 * See original copyright notice below.
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
import meta.Arguments;
import communication.threads.ForwardServerClientThread;

import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class ForwardClient {
    private static final boolean ENABLE_LOGGING = true;
    public static final int DEFAULTSERVERPORT = 2206;
    public static final String DEFAULTSERVERHOST = "localhost";
    public static final String PROGRAMNAME = "client.ForwardClient";

    private static Arguments arguments;
    private static int serverPort;
    private static String serverHost;

    /**
     * Program entry point. Reads arguments and run
     * the forward server
     */
    public static void main(String[] args)
    {
        try {
            arguments = new Arguments();
            arguments.setDefault("handshakeport", Integer.toString(DEFAULTSERVERPORT));
            arguments.setDefault("handshakehost", DEFAULTSERVERHOST);
            arguments.loadArguments(args);

            if (arguments.get("targetport") == null || arguments.get("targethost") == null) {
                throw new IllegalArgumentException("Target not specified");
            }

        } catch(IllegalArgumentException ex) {
            System.out.println(ex);
            usage();
            System.exit(1);
        }

        try {
            startForwardClient();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void doHandshake() throws IOException {
        /* Connect to forward server server */
        System.out.println("Connect to " +  arguments.get("handshakehost") + ":" + Integer.parseInt(arguments.get("handshakeport")));
        Socket socket = new Socket(arguments.get("handshakehost"), Integer.parseInt(arguments.get("handshakeport")));

        /* This is where the handshake should take place */

        //Step 1 Client Hello
        HandshakeMessage clientHello = new HandshakeMessage();

        clientHello.putParameter("MessageType", "ClientHello");
        clientHello.putParameter("Certificate", "ClientCert");
        clientHello.send(socket);

        //Step 3 receiving serverHello
        HandshakeMessage serverHello = new HandshakeMessage();
        serverHello.recv(socket);

        if (!serverHello.getParameter("MessageType").equals("ServerHello")) {
            System.out.println("Received invalid handshake type!");
            socket.close();
            throw new Error();
        }

        //Step 4 verifies serveHello

        String serverCert = serverHello.getParameter("Certificate");
        VerifyCertificate verifyCertificate = new VerifyCertificate("CaCertPath.pem", serverCert);

        if (!verifyCertificate.isUserVerified()) {
            socket.close();
            throw new Error();
        }

        //step 5 client request forwarding port
        HandshakeMessage forwardMessage = new HandshakeMessage();
        forwardMessage.putParameter("MessageType", "Forward");
        forwardMessage.putParameter("TargetHost", arguments.get("targethost"));
        forwardMessage.putParameter("TargetPort", arguments.get("targetport"));

        forwardMessage.send(socket);


        socket.close();
    }

    private static void setUpSession() {
        /*
         * Fake the communication.asdf.handshake result with static parameters.
         */

        /* This is to where the client.ForwardClient should connect.
         * The server.ForwardServer creates a socket
         * dynamically and communicates the address (hostname and port number)
         * to client.ForwardClient during the communication.asdf.handshake (ServerHost, ServerPort parameters).
         * Here, we use a static address instead.
         */
        serverHost = Handshake.serverHost;
        serverPort = Handshake.serverPort;
    }

    /*
     * Let user know that we are waiting
     */
    private static void tellUser(ServerSocket listensocket) throws UnknownHostException {
        System.out.println("Client forwarder to target " + arguments.get("targethost") + ":" + arguments.get("targetport"));
        System.out.println("Waiting for incoming connections at " +
                           InetAddress.getLocalHost().getHostAddress() + ":" + listensocket.getLocalPort());
    }

    /*
     * Set up client forwarder.
     * Run communication.asdf.handshake negotiation, then set up a listening socket and wait for user.
     * When user has connected, start port forwarder thread.
     */
    static public void startForwardClient() throws IOException {

        doHandshake();
        setUpSession();

        // Wait for client. Accept one connection.
        ForwardServerClientThread forwardThread;
        ServerSocket listensocket;

        try {
            /* Create a new socket. This is to where the user should connect.
             * client.ForwardClient sets up port forwarding between this socket
             * and the ServerHost/ServerPort learned from the handshake */
            listensocket = new ServerSocket();
            /* Let the system pick a port number */
            listensocket.bind(null);
            /* Tell the user, so the user knows where to connect */
            tellUser(listensocket);

            Socket clientSocket = listensocket.accept();
            String clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
            log("Accepted client from " + clientHostPort);

            forwardThread = new ForwardServerClientThread(clientSocket, serverHost, serverPort);
            forwardThread.start();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            throw e;
        }
    }

    /**
     * Prints given log message on the standart output if logging is enabled,
     * otherwise ignores it
     */
    public static void log(String aMessage)
    {
        if (ENABLE_LOGGING)
           System.out.println(aMessage);
    }

    static void usage() {
        String indent = "";
        System.err.println(indent + "Usage: " + PROGRAMNAME + " options");
        System.err.println(indent + "Where options are:");
        indent += "    ";
        System.err.println(indent + "--targethost=<hostname>");
        System.err.println(indent + "--targetport=<portnumber>");
        System.err.println(indent + "--handshakehost=<hostname>");
        System.err.println(indent + "--handshakeport=<portnumber>");
        System.err.println(indent + "--usercert=<filename>");
        System.err.println(indent + "--cacert=<filename>");
        System.err.println(indent + "--key=<filename>");
    }
}
