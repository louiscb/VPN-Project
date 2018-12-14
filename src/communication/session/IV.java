package communication.session;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class IV {
    private IvParameterSpec ivParameterSpec;
    private byte[] iv;

    public IV() throws Exception {
        createIV();
    }

    public IV(String ivString) {
        iv = ivString.getBytes();
        decodeIV();
    }

    private void createIV() throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        //Generate unique counter value for encryption using CTR
        SecureRandom randomSecureRandom = new SecureRandom();
        iv = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    public String encodeIV() { return Base64.getEncoder().encodeToString(iv); }

    private void decodeIV() {
        ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    }

    public IvParameterSpec getIvParameterSpec() {
        return ivParameterSpec;
    }
}
