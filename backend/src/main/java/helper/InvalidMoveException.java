package helper;

/**
 * Exception thrown when an invalid chess move is attempted.
 * 
 * <p>This exception is thrown in situations such as:
 * <ul>
 *   <li>Moving a piece against its movement rules</li>
 *   <li>Moving out of turn</li>
 *   <li>Moving into check</li>
 *   <li>Moving through other pieces</li>
 *   <li>Invalid castling attempts</li>
 *   <li>Invalid pawn promotion</li>
 *   <li>Moving a non-existent piece</li>
 *   <li>Invalid move notation</li>
 * </ul>
 * 
 * @see Position
 * @see Board
 * @version 1.0
 */
public class InvalidMoveException extends Exception {
    
    /**
     * Constructs a new InvalidMoveException with a detailed message.
     * 
     * @param msg Description of the invalid move condition
     */
    public InvalidMoveException(String msg) {
        super("Invalid Move: " + msg);
    }
}
