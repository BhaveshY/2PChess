package model;

import common.Colour;
import common.Direction;
import common.Position;
import utility.Log;

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

        Log.d(TAG, String.format("\n=== CHECKING PAWN MOVES ==="));
        Log.d(TAG, String.format("Pawn at %s, Color: %s", start, moverCol));

        // Check if pawn is in starting position
        boolean isInStartPosition = (moverCol == Colour.WHITE && start.getRow() == 6) || 
                                  (moverCol == Colour.BLACK && start.getRow() == 1);
        Log.d(TAG, String.format("Is in starting position: %s", isInStartPosition));

        // Forward moves
        Position oneStep = stepOrNull(this, new Direction[]{Direction.FORWARD}, start);
        if (oneStep != null) {
            Log.d(TAG, String.format("Checking forward move to %s", oneStep));
            // Can only move forward if the square is empty
            if (boardMap.get(oneStep) == null) {
                Log.d(TAG, "Forward square is empty - adding to possible moves");
                positionSet.add(oneStep);

                // Two steps forward from starting position if path is clear
                if (isInStartPosition) {
                    Position twoSteps = stepOrNull(this, new Direction[]{Direction.FORWARD, Direction.FORWARD}, start);
                    if (twoSteps != null && boardMap.get(twoSteps) == null) {
                        Log.d(TAG, String.format("Two-step move possible to %s", twoSteps));
                        positionSet.add(twoSteps);
                    }
                }
            }
        }

        // Check diagonal captures
        Log.d(TAG, "Checking diagonal captures:");
        
        // Left diagonal
        try {
            Position leftCapture = stepOrNull(this, new Direction[]{Direction.FORWARD, Direction.LEFT}, start);
            if (leftCapture != null) {
                BasePiece leftTarget = boardMap.get(leftCapture);
                Log.d(TAG, String.format("Left diagonal %s: %s", 
                    leftCapture,
                    leftTarget != null ? leftTarget.getColour() + " " + leftTarget.getClass().getSimpleName() : "empty"));
                if (leftTarget != null && leftTarget.getColour() != moverCol) {
                    Log.d(TAG, "Can capture left diagonal");
                    positionSet.add(leftCapture);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Left diagonal is off board");
        }

        // Right diagonal
        try {
            Position rightCapture = stepOrNull(this, new Direction[]{Direction.FORWARD, Direction.RIGHT}, start);
            if (rightCapture != null) {
                BasePiece rightTarget = boardMap.get(rightCapture);
                Log.d(TAG, String.format("Right diagonal %s: %s", 
                    rightCapture,
                    rightTarget != null ? rightTarget.getColour() + " " + rightTarget.getClass().getSimpleName() : "empty"));
                if (rightTarget != null && rightTarget.getColour() != moverCol) {
                    Log.d(TAG, "Can capture right diagonal");
                    positionSet.add(rightCapture);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Right diagonal is off board");
        }

        Log.d(TAG, String.format("Final possible moves: %s", positionSet));
        Log.d(TAG, "=== END PAWN MOVES ===\n");
        return positionSet;
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