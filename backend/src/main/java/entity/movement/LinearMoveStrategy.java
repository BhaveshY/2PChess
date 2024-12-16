package entity.movement;

import entity.BasePiece;
import helper.Direction;
import helper.Position;
import utility.MovementUtil;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reference implementation of MoveStrategy for pieces that move in straight lines
 * (Rook, Bishop, Queen). This class demonstrates how movement strategies can be
 * implemented in the future to improve code organization.
 * 
 * Note: This is currently not in use to maintain compatibility with existing code.
 * It can be gradually integrated later to improve the codebase structure.
 */
public class LinearMoveStrategy implements MoveStrategy {
    
    @Override
    public Set<Position> calculateMoves(BasePiece piece, Map<Position, BasePiece> boardMap, Position start) {
        Set<Position> positionSet = new HashSet<>();
        Direction[][] directions = piece.getDirections();

        for (Direction[] direction : directions) {
            Position current = MovementUtil.stepOrNull(piece, direction, start);
            while (current != null) {
                // Stop if we hit a piece of the same color
                BasePiece targetPiece = boardMap.get(current);
                if (targetPiece != null) {
                    if (targetPiece.getColour() != piece.getColour()) {
                        positionSet.add(current); // Can capture opponent's piece
                    }
                    break;
                }
                positionSet.add(current);
                current = MovementUtil.stepOrNull(piece, direction, current);
            }
        }
        
        return positionSet;
    }
} 