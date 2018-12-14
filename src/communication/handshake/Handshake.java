package communication.handshake;

public class Handshake {
    /* Where the client forwarder forwards data from  */
    public static final String serverHost = "localhost";
    public static final int serverPort = 4412;    

    /* The final destination */
    public static String targetHost = "localhost";
    public static int targetPort = 6789;

    public static String getServerHost() {
        return serverHost;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getTargetHost() {
        return targetHost;
    }

    public static void setTargetHost(String targetHost) {
        Handshake.targetHost = targetHost;
    }

    public static int getTargetPort() {
        return targetPort;
    }

    public static void setTargetPort(int targetPort) {
        Handshake.targetPort = targetPort;
    }
}
