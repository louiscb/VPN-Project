import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SessionKey {
    private SecretKey secretKey;

    SessionKey (Integer keyLength) {
        createSecretKey(keyLength);
    }

    SessionKey (String encodedKey) {
        decodeKey(encodedKey);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String encodeKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(encodedKey);
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
        System.out.println("ENCODE LENGTH " + secretKey.getEncoded().length);

    }

    private void decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        System.out.println("DECODED LENGTH " + decodedKey.length);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}