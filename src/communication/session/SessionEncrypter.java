package communication.session;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Base64;

public class SessionEncrypter {
    public SessionKey sessionKey;
    public IvParameterSpec ivParameterSpec;
    byte[] iv;

    public void createSessionKey (Integer keyLength) {
        sessionKey = new SessionKey(keyLength);
    }

    public void createIV () {
        ivParameterSpec = null;
    }

    public String encodeKey() {
        return sessionKey.encodeKey();
    }

    public String encodeIV() { return Base64.getEncoder().encodeToString(iv); }

    public CipherOutputStream openCipherOutputStream(OutputStream output) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            //Generate unique counter value for encryption using CTR
            SecureRandom randomSecureRandom = new SecureRandom();
            iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);

//            for (byte i: iv) {
//                System.out.println(i);
//            }

            //System.out.println(new String(iv, "UTF-8"));
            ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, sessionKey.getSecretKey(), ivParameterSpec);

            return new CipherOutputStream(output,cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}