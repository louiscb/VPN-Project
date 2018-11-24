import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.util.Base64;

public class SessionDecrypter {
    SessionKey sessionKey;
    IvParameterSpec iv;

    SessionDecrypter (String key, String iv) {
        sessionKey = new SessionKey(key);
        this.iv = new IvParameterSpec(Base64.getDecoder().decode(iv));
    }

    CipherInputStream openCipherInputStream (InputStream input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, sessionKey.getSecretKey(), iv);
            return new CipherInputStream(input, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
