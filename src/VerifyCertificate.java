/*
Class that checks x509 openSSL certificates

Should accept two parameters.

The first parameter is the file with the CA certificate, and the second parameter is the file with the user certificate. When you run your program, it should do the following:

Print the DN for the CA (one line)
Print the DN for the user (one line)
Verify the CA certificate
Verify the user certificate
Print "Pass" if check 3 and 4 are successful
Print "Fail" if any of them fails, followed by an explanatory comment of how the verification failed
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class VerifyCertificate {
    X509Certificate CACert;
    X509Certificate userCert;

    VerifyCertificate (String CACertPath, String userCertPath) {
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");

            FileInputStream CAIs = new FileInputStream(CACertPath);
            CACert = (X509Certificate) fact.generateCertificate(CAIs);

            FileInputStream userIs = new FileInputStream(userCertPath);
            userCert = (X509Certificate) fact.generateCertificate(userIs);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    
}
