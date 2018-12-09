package communication.handshake;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.*;

public class VerifyCertificate {
    private X509Certificate CACert;
    private X509Certificate userCert;

    public VerifyCertificate (String CACertPath, String userCertPath) {
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");

            FileInputStream CAIs = new FileInputStream(CACertPath);
            CACert = (X509Certificate) fact.generateCertificate(CAIs);

            FileInputStream userIs = new FileInputStream(userCertPath);
            userCert = (X509Certificate) fact.generateCertificate(userIs);

        } catch (CertificateException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printCACertDN () {
        System.out.println("Here is the CA's DN");
        System.out.println(CACert.getSubjectDN());
    }

    public void printUserCertDN () {
        System.out.println("Here is the User's DN");
        System.out.println(userCert.getSubjectDN());
    }

    public void verify() {
        if (isCAVerified() && isUserVerified()) {
            System.out.println("\nPASS");
        } else {
            System.out.println("\nFAIL");
        }
    }

    private boolean isCAVerified() {
        System.out.println("\nVerifying CA's certificate:");

        if (!isDateValid(CACert))
            return false;

        //Do I need to check that the CA is self verified?

        return true;
    }

    private boolean isUserVerified() {
        System.out.println("\nVerifying user's certificate:");

        if (!isDateValid(CACert))
            return false;

        //Verifies that user certificate was signed using the private key
        //that corresponds to the CA's public key
        try {
            userCert.verify(CACert.getPublicKey());
            System.out.println("-> User certificate is signed by CA");
        } catch (Exception e) {
            System.out.println("ERROR: User certificate not signed by CA");
            return false;
        }

        return true;
    }

    private boolean isDateValid(X509Certificate certificate) {
        //checking date & expiration of cert
        try {
            certificate.checkValidity();
            System.out.println("-> The certificate has not expired & is valid");
            return true;
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            System.out.println("ERROR: Certificate's dates are not valid");
            return false;
        }
    }
}