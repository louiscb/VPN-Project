package communication.session;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.OutputStream;

public class SessionEncrypter {
    private SessionKey sessionKey;
    private IV iv;

    public SessionEncrypter(SessionKey sessionKey, IV iv) {
        this.sessionKey = sessionKey;
        this.iv = iv;
    }

    public String encodeKey() {
        return sessionKey.encodeKey();
    }

    public String encodeIV() { return iv.encodeIV(); }

    public CipherOutputStream openCipherOutputStream(OutputStream output) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, sessionKey.getSecretKey(), iv.getIvParameterSpec());

        return new CipherOutputStream(output,cipher);
    }
}