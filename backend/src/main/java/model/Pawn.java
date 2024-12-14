package model;

import common.Colour;
import common.Direction;
import common.Position;
import utility.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static utility.MovementUtil.stepOrNull;

/**
 * Pawn class extends BasePiece. Move directions for the Pawn, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class Pawn extends BasePiece {

    private static final String TAG = "PAWN";

    /**
     * Pawn constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public Pawn(Colour colour) {
        super(colour);
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    public void setupDirections() {
        // Only set up basic forward movement, diagonal captures will be handled separately
        this.directions = new Direction[][] {
                {Direction.FORWARD}
        };
    }

    /**
     * Fetch all the possible positions where a piece can move on board
     * @param boardMap: Board Map instance representing current game board
     * @param start: position of piece on board
     * @return Set of possible positions a piece is allowed to move
     * */
    @Override
    public Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start) {
        Set<Position> positionSet = new HashSet<>();
        Colour moverCol = this.getColour();

        // Check if pawn is in starting position
        boolean isInStartPosition = (moverCol == Colour.WHITE && start.getRow() == 6) || 
                                  (moverCol == Colour.BLACK && start.getRow() == 1);

        // Forward moves
        Position oneStep = stepOrNull(this, new Direction[]{Direction.FORWARD}, start);
        if (oneStep != null) {
            // Can only move forward if the square is empty
            if (boardMap.get(oneStep) == null) {
                positionSet.add(oneStep);

                // Two steps forward from starting position if path is clear
                if (isInStartPosition) {
                    Position twoSteps = stepOrNull(this, new Direction[]{Direction.FORWARD, Direction.FORWARD}, start);
                    if (twoSteps != null && boardMap.get(twoSteps) == null) {
                        positionSet.add(twoSteps);
                    }
                }
            }
        }

        // Diagonal captures
        checkDiagonalCapture(boardMap, start, Direction.LEFT, moverCol, positionSet);
        checkDiagonalCapture(boardMap, start, Direction.RIGHT, moverCol, positionSet);

        return positionSet;
    }

    /**
     * Helper method to check diagonal captures
     */
    private void checkDiagonalCapture(Map<Position, BasePiece> boardMap, Position start, 
                                    Direction direction, Colour moverCol, Set<Position> positionSet) {
        try {
            // First move forward, then left/right for diagonal
            Position capturePos = start.neighbour(Direction.FORWARD);
            if (capturePos != null) {
                capturePos = capturePos.neighbour(direction);
                if (capturePos != null) {
                    BasePiece target = boardMap.get(capturePos);
                    // Can only capture enemy pieces
                    if (target != null && target.getColour() != moverCol) {
                        positionSet.add(capturePos);
                    }
                }
            }
        } catch (Exception e) {
            // Position is off the board, ignore
        }
    }

    /**
     * Returns custom string representation of the class
     * @return String
     * */
    @Override
    public String toString() {
        return this.colour.toString() + "P";
    }
}