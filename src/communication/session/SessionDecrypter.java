package communication.session;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.InputStream;

public class SessionDecrypter {
    private SessionKey sessionKey;
    private IV iv;

    public SessionDecrypter(SessionKey key, IV iv) {
        sessionKey = key;
        this.iv = iv;
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

    public SessionKey getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    public IV getIv() {
        return iv;
    }

    public void setIv(IV iv) {
        this.iv = iv;
    }
}
