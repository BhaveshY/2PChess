package entity.movement;

import entity.BasePiece;
import helper.Position;
import java.util.Map;
import java.util.Set;

/**
 * Interface defining movement strategy following Open/Closed Principle.
 * New movement patterns can be added by implementing this interface
 * without modifying existing code.
 */
public interface MoveStrategy {
    /**
     * Calculate possible moves for a piece
     * @param piece The piece to calculate moves for
     * @param boardMap Current state of the board
     * @param start Starting position of the piece
     * @return Set of valid positions the piece can move to
     */
    Set<Position> calculateMoves(BasePiece piece, Map<Position, BasePiece> boardMap, Position start);
} 