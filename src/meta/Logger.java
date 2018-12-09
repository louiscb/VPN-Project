package meta;

public class Logger {
    private static final boolean ENABLE_LOGGING = true;
    public static void log(String message) {
        if (ENABLE_LOGGING) {
            System.out.println(message);
        }
    }
}
