package communication.session;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.InputStream;

public class SessionDecrypter {
    SessionKey sessionKey;
    IV iv;

    public SessionDecrypter(String key, String iv) {
        sessionKey = new SessionKey(key);
        this.iv = new IV(iv);
    }

    public CipherInputStream openCipherInputStream (InputStream input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, sessionKey.getSecretKey(), iv.getIvParameterSpec());
            return new CipherInputStream(input, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
