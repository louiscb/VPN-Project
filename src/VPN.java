public class VPN {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        //testSessionKey();
        testSessionCrypto();
    }

    private static void testSessionKey() {
        SessionKey key1 = new SessionKey(128);
        SessionKey key2 = new SessionKey(key1.encodeKey());

        if (key1.getSecretKey().equals(key2.getSecretKey())) {
            System.out.println("The keys match");
        }
        else {
            System.out.println("The keys do not match");
        }
    }

    private static void testSessionCrypto() {
        TestSessionCrypto testSessionCrypto = new TestSessionCrypto();

        try {
            testSessionCrypto.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
