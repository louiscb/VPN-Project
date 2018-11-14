import java.util.Base64;

public class SessionKey {
    private int keyLength;
    private String encodedKey;
    private String secretKey;

    SessionKey (Integer keyLength) {
        this.keyLength = keyLength;
        createSecretKey(keyLength);
    }

    SessionKey (String encodedKey) {
        this.encodedKey = encodedKey;
        decodeKey(encodedKey);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void encodeKey() {

    }

    private void createSecretKey(Integer keyLength) {

    }

    private void decodeKey(String encodedKey) {

    }
}