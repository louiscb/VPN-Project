import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;

public class SessionEncrypter {
    SessionKey sessionKey;
    IvParameterSpec ivParameterSpec;
    byte[] iv;

    SessionEncrypter (Integer keyLength) {
        sessionKey = new SessionKey(keyLength);
    }

    String encodeKey() {
        return sessionKey.encodeKey();
    }

    String encodeIV() {
        String returnIV = Base64.getEncoder().encodeToString(iv);
        System.out.println("HERE");
        System.out.println(returnIV.length());
        return returnIV;
    }

    CipherOutputStream openCipherOutputStream(OutputStream output) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            //Generate unique counter value for encryption using CTR
            SecureRandom randomSecureRandom = new SecureRandom();
            iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);
            System.out.println(new String(iv, "UTF-8"));
            ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, sessionKey.getSecretKey(), ivParameterSpec);
            return new CipherOutputStream(output,cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 }
