package communication.session;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SessionKey {
    private SecretKey secretKey;

    public SessionKey(Integer keyLength) {
        createSecretKey(keyLength);
    }

    public SessionKey (String encodedKey) {
        decodeKey(encodedKey);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String encodeKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

    private void createSecretKey(Integer keyLength) {
        KeyGenerator keyGenerator = null;

        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyGenerator.init(keyLength);

        secretKey = keyGenerator.generateKey();
    }

    private void decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}