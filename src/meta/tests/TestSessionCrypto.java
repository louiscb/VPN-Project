package meta.tests;

import communication.session.SessionDecrypter;
import communication.session.SessionEncrypter;

import java.io.*;
import javax.crypto.*;

public class TestSessionCrypto  {
    static String PLAININPUT = "plaininput";
    static String PLAINOUTPUT = "plainoutput";
    static String CIPHER = "cipher";
    static Integer KEYLENGTH = 128;

    public void run() throws Exception {
        int b;
/*
        // Create encrypter instance for a given key length
        SessionEncrypter sessionEncrypter = new SessionEncrypter(KEYLENGTH);

        // Attach output file to encrypter, and open input file
        try (
                CipherOutputStream cryptoOut = sessionEncrypter.openCipherOutputStream(new FileOutputStream(CIPHER));
                FileInputStream plainIn = new FileInputStream(PLAININPUT);
        ) {
            // Copy data byte by byte from plain input to crypto output via encrypter
            while ((b = plainIn.read()) != -1) {
                cryptoOut.write(b);
            }
        }

        // Now ciphertext is in cipher output file. Decrypt it back to plaintext.

        // Create decrypter instance using cipher parameters from encrypter
        SessionDecrypter sessionDecrypter = new SessionDecrypter(sessionEncrypter.encodeKey(), sessionEncrypter.encodeIV());

        // Attach input file to decrypter, and open output file
        try (
                CipherInputStream cryptoIn = sessionDecrypter.openCipherInputStream(new FileInputStream(CIPHER));
                FileOutputStream plainOut = new FileOutputStream(PLAINOUTPUT);
        ) {
            // Copy data byte by byte from cipher input to plain output via decrypter
            while ((b = cryptoIn.read()) != -1) {
                plainOut.write(b);
            }
        }
*/
        System.out.format("Encryption and decryption done. Check that \"%s\" and \"%s\" are identical!\n", PLAININPUT, PLAINOUTPUT);
    }
}