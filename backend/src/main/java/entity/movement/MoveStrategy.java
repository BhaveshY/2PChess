package entity.movement;

import entity.BasePiece;
import helper.Position;
import java.util.Map;
import java.util.Set;

/**
 * Defines the strategy for calculating valid moves for chess pieces.
 * 
 * <p>This interface provides a contract for implementing different movement patterns
 * for chess pieces. Each piece type can have its own implementation of this interface
 * to define its unique movement rules.
 * 
 * <p>Movement strategies can include:
 * <ul>
 *   <li>Linear movement (Rook, Bishop, Queen)</li>
 *   <li>L-shaped movement (Knight)</li>
 *   <li>Single square movement (King)</li>
 *   <li>Directional movement (Pawn)</li>
 * </ul>
 * 
 * [Interface Segregation Principle]
 * Interface defining movement strategy following Open/Closed Principle.
 * New movement patterns can be added by implementing this interface
 * without modifying existing code.
 * 
 * @see BasePiece
 * @see Position
 * @version 1.0
 */
public interface MoveStrategy {
    /**
     * Calculates all valid moves for a piece from its current position.
     * 
     * <p>This method considers:
     * <ul>
     *   <li>The piece's movement rules</li>
     *   <li>Current board state</li>
     *   <li>Piece position</li>
     *   <li>Board boundaries</li>
     *   <li>Other pieces blocking movement</li>
     * </ul>
     * 
     * @param piece The piece to calculate moves for
     * @param boardMap Current state of the board
     * @param start Starting position of the piece
     * @return Set of valid positions the piece can move to
     */
    Set<Position> calculateMoves(BasePiece piece, Map<Position, BasePiece> boardMap, Position start);
} 