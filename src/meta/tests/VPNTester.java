package meta.tests;

import communication.handshake.aCertificate;

import java.security.cert.X509Certificate;

public class VPNTester {
    public void main(String[] args) {
        testaCertificate();
        /*test();*/
    }

    private void testaCertificate() {
        String testS = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDjjCCAnYCCQDjsWS7GJ4tMjANBgkqhkiG9w0BAQsFADCBiDELMAkGA1UEBhMC\n" +
                "U0UxEjAQBgNVBAgMCVN0b2NraG9sbTEOMAwGA1UEBwwFS2lzdGExDDAKBgNVBAoM\n" +
                "A0tUaDEMMAoGA1UECwwDSUNUMRowGAYDVQQDDBFJbnRlcm5ldCBTZWN1cml0eTEd\n" +
                "MBsGCSqGSIb3DQEJARYObG91aXNjYkBrdGguc2UwHhcNMTgxMTI3MjI0ODQwWhcN\n" +
                "MjEwODIzMjI0ODQwWjCBiDELMAkGA1UEBhMCU0UxEjAQBgNVBAgMCVN0b2NraG9s\n" +
                "bTEOMAwGA1UEBwwFS2lzdGExDDAKBgNVBAoMA0tUaDEMMAoGA1UECwwDSUNUMRow\n" +
                "GAYDVQQDDBFJbnRlcm5ldCBTZWN1cml0eTEdMBsGCSqGSIb3DQEJARYObG91aXNj\n" +
                "YkBrdGguc2UwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCgmCBuI2g8\n" +
                "19D3S4PLmvOpw1b8+nCEYm1ts5wlp4whL5DT81Y4rE+iPSY8IkExm7/BQk7PR1Yg\n" +
                "rWbI/Wi0NDYauTZyArW4TO0beL46/wgd0PHVB8OB89JkQbpgGDhYQAP2obGpgURe\n" +
                "hJVO608T3ZksEZm2pNLC1OprywG86f8Ojows8WYlU74p6j0MrqmBOYxJPbq3OLs5\n" +
                "IgnnJXtdAkW9RdlCmZ2XElYwAbQrytVtvCtsSh1bLz4jcqycUUO7m9+YVD5+PKfO\n" +
                "0hnttgfyDphIYexgliXGSeM+sz61Nz8kTqYL3X6CutObDHGJt8D8mcZViM2XDUkA\n" +
                "qpOno48SvFMNAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAI/RqXFBxFRID85oQZe7\n" +
                "6qPeok+DaP9LG8NvnpdcXlweJ2bFJzuLXfNq0CmdGNgASQOv5HpMPz5tfZxm3mYt\n" +
                "Xhxy6Jwu1iukvtAiJenRQFjaeSgg11ORZZ9DjuxUUZaKIi+eYMZV0fSniGOBW8DJ\n" +
                "bCZX85Wrl8JAdG2O0UUiq43t7fiCAIe7mVcfhYKIKsRqUl7er5O9bJsiviskrmBV\n" +
                "v6b7GEniLaoyUzMM4zxEpcyIFumDavPQcMrPcHbTmGzTatlBCRMppR8EseJbjkcJ\n" +
                "xyUTDOon+O3w/SnmxVYNcvxoQdaBa4QY2Vik/8gL9zogJlUEFqGUKIICJ6MdSJy9\n" +
                "mL4=\n" +
                "-----END CERTIFICATE-----\n";
        try {
            X509Certificate cert = aCertificate.stringToCert(testS);

            System.out.println(cert.getIssuerDN());

            System.out.println(aCertificate.stringToCert(aCertificate.encodeCert(cert)).getIssuerDN());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
/*
    private static void test() {
        System.out.println(" - Test 1: - ");
        testSessionKey();
        System.out.println();

        System.out.println(" - Test 2: - ");
        testSessionCrypto();
        System.out.println();

        System.out.println(" - Test 3: - ");
        testVerifyCertificate();
        System.out.println();

        System.out.println(" - Test 4: - ");
        testHandshakeCrypto();
    }

    private static void testVerifyCertificate() {
        HandleCertificate vC = new HandleCertificate("certs/CA.pem", "certs/user.pem");
        vC.verify();
    }

    private static void testSessionKey() {
        SessionKey key1 = new SessionKey(128);
        SessionKey key2 = new SessionKey(key1.encodeKey());

        if (key1.getSecretKey().equals(key2.getSecretKey())) {
            System.out.println("The keys match");
        } else {
            System.out.println("The keys do not match");
        }
    }

    private static void testSessionCrypto() {
        TestSessionCrypto testSessionCrypto = new TestSessionCrypto();

        try {
            testSessionCrypto.encrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testHandshakeCrypto() {
        try {
            HandshakeCryptoTester.encrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
