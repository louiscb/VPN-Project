package communication.session;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.OutputStream;

public class SessionEncrypter {
    private SessionKey sessionKey;
    private IV iV;

    public SessionEncrypter(String sessionKey, String ivString) {
        this.sessionKey = new SessionKey(sessionKey);
        this.iV = new IV(ivString);
    }

    public String encodeKey() {
        return sessionKey.encodeKey();
    }

    public String encodeIV() { return iV.encodeIV(); }

    public CipherOutputStream openCipherOutputStream(OutputStream output) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, sessionKey.getSecretKey(), iV.getIvParameterSpec());

        return new CipherOutputStream(output,cipher);
    }
}