package communication.handshake;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class aCertificate {
    private X509Certificate cert;

    public X509Certificate getCert() {
        return cert;
    }

    //takes a path and converts it to a cert object
    public void pathToCert(String certPath) throws Exception {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");

        FileInputStream CAIs = new FileInputStream(certPath);
        cert = (X509Certificate) fact.generateCertificate(CAIs);
    }
    //takes a string and converts it to a cert object
    //string has to have line breaks!
    public void stringToCert(String certString) throws Exception {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(certString.getBytes());
        cert = (X509Certificate) fact.generateCertificate(is);
    }

    //takes a cert object and converts it to a string
    public String encodeCert() throws Exception {
        String LINE_SEPARATOR = System.getProperty("line.separator");
        String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
        String END_CERT = "-----END CERTIFICATE-----";

        Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());

        byte[] rawCrtText = cert.getEncoded();
        String encodedCertText = new String(encoder.encode(rawCrtText));
        return BEGIN_CERT + LINE_SEPARATOR + encodedCertText + LINE_SEPARATOR + END_CERT;
    }
}
