package communication.handshake;

import java.security.cert.*;

public class HandleCertificate {
    private X509Certificate CACert;
    private X509Certificate userCert;

    //have to pass CA file
    public HandleCertificate(String CACertPath) throws Exception {
        CACert = aCertificate.pathToCert(CACertPath);
    }

    public boolean verify(X509Certificate userCert) {
        this.userCert = userCert;
        return (isCAVerified() && isUserVerified());
    }

    private boolean isCAVerified() {
        //System.out.println("\nVerifying CA's certificate:");

        return isDateValid(CACert);
    }

    private boolean isUserVerified() {
       // System.out.println("\nVerifying user's certificate:");

        if (!isDateValid(CACert))
            return false;

        //Verifies that user certificate was signed using the private key
        //that corresponds to the CA's public key
        try {
            userCert.verify(CACert.getPublicKey());
        //    System.out.println("-> User certificate is signed by CA");
        } catch (Exception e) {
            System.err.println("ERROR: User certificate not signed by CA");
            return false;
        }

        return true;
    }

    private boolean isDateValid(X509Certificate certificate) {
        //checking date & expiration of cert
        try {
            certificate.checkValidity();
           // System.out.println("-> The certificate has not expired & is valid");
            return true;
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            System.err.println("ERROR: Certificate's dates are not valid");
            return false;
        }
    }

    //    public void printCACertDN () {
//        System.out.println("Here is the CA's DN");
//        System.out.println(CACert.getSubjectDN());
//    }
//
//    public void printUserCertDN () {
//        System.out.println("Here is the User's DN");
//        System.out.println(userCert.getSubjectDN());
//    }
}