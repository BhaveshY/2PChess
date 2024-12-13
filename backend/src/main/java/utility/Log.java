package utility;

/**
 * Log Class to facilitate logging
 **/
public class Log {

    /**
     * method to print information to debug
     **/
    public static void d(String tag, Object message) {
        System.out.println(tag+" : " + message);
    }

    /**
     * method to error information
     **/
    public static void e(String tag, Object message) {
        System.err.println(tag+" : " + message);
    }
}
