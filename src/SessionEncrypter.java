import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.OutputStream;

public class SessionEncrypter {
    SessionKey sessionKey;

    SessionEncrypter (Integer keyLength) {
        sessionKey = new SessionKey(keyLength);
    }

    String encodeKey() {
        return sessionKey.encodeKey();
    }

    String encodeIV() {
        return null;
    }

    CipherOutputStream openCipherOutputStream(OutputStream output) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey.getSecretKey());
            return new CipherOutputStream(output,cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 }
