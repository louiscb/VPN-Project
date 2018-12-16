package meta;

public class Common {
    public static final int KEY_LENGTH = 128;

    //PARAMETERS FOR HANDSHAKE MESSAGES
    public static final String MESSAGE_TYPE = "MessageType";
    public static final String CLIENT_HELLO = "ClientHello";
    public static final String SERVER_HELLO = "ServerHello";
    public static final String FORWARD_MSG = "ForwardMessage";
    public static final String CERTIFICATE = "Certificate";
    public static final String TARGET_HOST = "TargetHost";
    public static final String TARGET_PORT = "TargetPort";

    //PATHS TO CERT FILES
    public static final String CA_PATH = "certs/CA.pem";
    public static final String SERVER_CERT_PATH = "certs/user.pem";
    public static final String CLIENT_CERT_PATH = "certs/user.pem";
}
