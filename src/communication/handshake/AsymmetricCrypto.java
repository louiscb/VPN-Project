package communication.handshake;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class AsymmetricCrypto {
    public static String encrypt(String plaintext, Key key) throws Exception {
        //byte[] encoded = Base64.getEncoder().encode(plaintext.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return new String(Base64.getEncoder().encode(cipher.doFinal(plaintext.getBytes())));
    }

    public static String decrypt(String ciphertext, Key key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(ciphertext.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        return new String(cipher.doFinal(decoded));
    }

    public static PublicKey getPublicKeyFromCertFile(String certfile) {
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            FileInputStream CAIs = new FileInputStream(certfile);
            return fact.generateCertificate(CAIs).getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PrivateKey getPrivateKeyFromKeyFile(String keyfile) {
        try {
            Path path = Paths.get(keyfile);
            byte[] privKeyBytes = Files.readAllBytes(path);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
