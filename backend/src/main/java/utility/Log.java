package utility;

/**
 * Utility class for application logging.
 * 
 * <p>This class provides a simple logging interface for:
 * <ul>
 *   <li>Debug information</li>
 *   <li>Error messages</li>
 *   <li>Application state tracking</li>
 *   <li>Development troubleshooting</li>
 * </ul>
 * 
 * <p>The class uses standard output streams for logging:
 * <ul>
 *   <li>System.out for debug messages</li>
 *   <li>System.err for error messages</li>
 * </ul>
 * 
 * @version 1.0
 */
public class Log {

    /**
     * Logs a debug message.
     * 
     * <p>Used for:
     * <ul>
     *   <li>Development debugging</li>
     *   <li>State tracking</li>
     *   <li>Flow control monitoring</li>
     * </ul>
     * 
     * @param tag Component identifier (e.g., class name)
     * @param message Debug information to log
     */
    public static void d(String tag, Object message) {
        System.out.println(tag + " : " + message);
    }

    /**
     * Logs an error message.
     * 
     * <p>Used for:
     * <ul>
     *   <li>Error conditions</li>
     *   <li>Exception handling</li>
     *   <li>Invalid states</li>
     *   <li>Runtime problems</li>
     * </ul>
     * 
     * @param tag Component identifier (e.g., class name)
     * @param message Error information to log
     */
    public static void e(String tag, Object message) {
        System.err.println(tag + " : " + message);
    }
}
