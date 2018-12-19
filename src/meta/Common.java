package meta;

public class Common {
    public static final int KEY_LENGTH = 128;

    //PARAMETERS FOR HANDSHAKE MESSAGES
    public static final String MESSAGE_TYPE = "MessageType";
    public static final String CLIENT_HELLO = "ClientHello";
    public static final String SERVER_HELLO = "ServerHello";
    public static final String FORWARD_MSG = "Forward";
    public static final String CERTIFICATE = "Certificate";
    public static final String TARGET_HOST = "TargetHost";
    public static final String TARGET_PORT = "TargetPort";
    public static final String SESSION_MSG = "Session";
    public static final String SESSION_KEY = "SessionKey";
    public static final String SESSION_IV = "SessionIV";

    //PATHS TO CERT FILES
    public static final String CA_PATH = "certs/new/CA.pem";
    public static final String SERVER_CERT_PATH = "certs/new/server.pem";
    public static final String CLIENT_CERT_PATH = "certs/new/client.pem";
    public static final String CLIENT_PRIV_KEY_PATH = "certs/new/clientprivatekey.der";
}
