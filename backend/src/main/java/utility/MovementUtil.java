package utility;

import helper.Direction;
import helper.InvalidPositionException;
import helper.Position;
import entity.BasePiece;

/**
 * Utility class for calculating chess piece movements.
 * 
 * <p>This class provides helper methods for:
 * <ul>
 *   <li>Calculating next positions based on movement directions</li>
 *   <li>Handling boundary conditions</li>
 *   <li>Managing movement failures gracefully</li>
 *   <li>Supporting both forward and reverse movement</li>
 * </ul>
 * 
 * <p>The class is designed to be stateless and thread-safe, with all methods
 * being static utility functions.
 * 
 * @see Position
 * @see Direction
 * @see BasePiece
 * @version 1.0
 */
public class MovementUtil {

    /**
     * Calculates the next position after moving in specified directions.
     * 
     * <p>This method:
     * <ul>
     *   <li>Takes an array of directions to move in sequence</li>
     *   <li>Applies each direction to calculate the final position</li>
     *   <li>Validates each step against board boundaries</li>
     * </ul>
     * 
     * @param piece Piece being moved
     * @param step Array of directions to move in sequence
     * @param current Starting position
     * @return Final position after applying all movement steps
     * @throws InvalidPositionException if any step would move off the board
     */
    public static Position step(BasePiece piece, Direction[] step, Position current) throws InvalidPositionException {
        return current.move(step);
    }

    /**
     * Calculates the next position with optional reverse movement.
     * 
     * <p>Similar to {@link #step(BasePiece, Direction[], Position)} but with
     * support for reverse movement direction. This is useful for pieces that
     * can move backwards or have special reverse movement rules.
     * 
     * @param piece Piece being moved
     * @param step Array of directions to move in sequence
     * @param current Starting position
     * @param reverse Whether to move in reverse direction
     * @return Final position after applying all movement steps
     * @throws InvalidPositionException if any step would move off the board
     */
    public static Position step(BasePiece piece, Direction[] step, Position current, boolean reverse) throws InvalidPositionException {
        return current.move(step);
    }

    /**
     * Safe version of step that returns null instead of throwing exceptions.
     * 
     * <p>This method is useful when:
     * <ul>
     *   <li>Calculating possible moves where some may be invalid</li>
     *   <li>Checking move validity without exception handling</li>
     *   <li>Building move sets incrementally</li>
     * </ul>
     * 
     * @param piece Piece being moved
     * @param step Array of directions to move in sequence
     * @param current Starting position
     * @return Final position after moves, or null if any step is invalid
     */
    public static Position stepOrNull(BasePiece piece, Direction[] step, Position current) {
        try {
            return step(piece, step, current);
        } catch (InvalidPositionException e) {
            return null;
        }
    }

    /**
     * Safe version of step with reverse option that returns null instead of throwing exceptions.
     * 
     * <p>Combines the safety of {@link #stepOrNull(BasePiece, Direction[], Position)}
     * with the reverse movement capability of 
     * {@link #step(BasePiece, Direction[], Position, boolean)}.
     * 
     * @param piece Piece being moved
     * @param step Array of directions to move in sequence
     * @param current Starting position
     * @param reverse Whether to move in reverse direction
     * @return Final position after moves, or null if any step is invalid
     */
    public static Position stepOrNull(BasePiece piece, Direction[] step, Position current, boolean reverse) {
        try {
            return step(piece, step, current, reverse);
        } catch (InvalidPositionException e) {
            return null;
        }
    }
}
