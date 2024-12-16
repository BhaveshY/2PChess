package entity.movement;

import entity.BasePiece;
import helper.Direction;
import helper.Position;
import utility.MovementUtil;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements movement strategy for pieces that move in straight lines.
 * 
 * <p>This strategy applies to pieces that move linearly on the board:
 * <ul>
 *   <li>Rook - horizontal and vertical lines</li>
 *   <li>Bishop - diagonal lines</li>
 *   <li>Queen - all directions</li>
 * </ul>
 * 
 * <p>The strategy handles:
 * <ul>
 *   <li>Continuous movement until blocked</li>
 *   <li>Capture of opponent pieces</li>
 *   <li>Board boundary detection</li>
 * </ul>
 * 
 * Reference implementation of MoveStrategy for pieces that move in straight lines
 * (Rook, Bishop, Queen). This class demonstrates how movement strategies can be
 * implemented in the future to improve code organization.
 * 
 * Note: This is currently not in use to maintain compatibility with existing code.
 * It can be gradually integrated later to improve the codebase structure.
 * 
 * @see MoveStrategy
 * @see BasePiece
 * @version 1.0
 */
public class LinearMoveStrategy implements MoveStrategy {
    
    /**
     * Calculates all possible moves for a piece that moves in straight lines.
     * 
     * <p>The calculation process:
     * <ol>
     *   <li>For each direction the piece can move:</li>
     *   <li>Continue moving in that direction until:</li>
     *   <ul>
     *     <li>Board boundary is reached</li>
     *     <li>Another piece is encountered</li>
     *     <li>If piece is opponent's, include capture move</li>
     *   </ul>
     * </ol>
     * 
     * @param piece The piece to calculate moves for
     * @param boardMap Current state of the board
     * @param start Starting position of the piece
     * @return Set of all valid positions the piece can move to
     */
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