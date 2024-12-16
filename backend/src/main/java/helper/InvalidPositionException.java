package helper;

/**
 * Exception thrown when an invalid chess board position is encountered.
 * 
 * <p>This exception is thrown in situations such as:
 * <ul>
 *   <li>Coordinates outside the board boundaries (0-7)</li>
 *   <li>Invalid color space combinations</li>
 *   <li>Attempting to access non-existent positions</li>
 *   <li>Invalid algebraic notation conversion</li>
 * </ul>
 * 
 * @see Position
 * @version 1.0
 */
public class InvalidPositionException extends Exception {
    
    /**
     * Constructs a new InvalidPositionException with a detailed message.
     * 
     * @param msg Description of the invalid position condition
     */
    public InvalidPositionException(String msg) {
        super("Invalid Position: " + msg);
    }
}
