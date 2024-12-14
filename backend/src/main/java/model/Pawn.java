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
        this.directions = new Direction[][] {
                {Direction.FORWARD},
                {Direction.FORWARD, Direction.FORWARD},
                {Direction.FORWARD, Direction.LEFT},
                {Direction.LEFT, Direction.FORWARD},
                {Direction.FORWARD, Direction.RIGHT},
                {Direction.RIGHT, Direction.FORWARD}
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
        Direction[][] steps = this.directions;

        // Check if pawn is in starting position
        boolean isInStartPosition = (moverCol == Colour.WHITE && start.getRow() == 6) || 
                                  (moverCol == Colour.BLACK && start.getRow() == 1);

        for (int i = 0; i < steps.length; i++) {
            Direction[] step = steps[i];
            Position end = stepOrNull(this, step, start);

            if (end != null && !positionSet.contains(end)) {
                BasePiece target = boardMap.get(end);
                Log.d(TAG, String.format("Checking move from %s to %s (step: %s)", start, end, Arrays.toString(step)));

                try {
                    // One step forward - only if square is empty
                    boolean isOneStepForward = (i == 0 && target == null);

                    // Two steps forward - only from starting position and if both squares are empty
                    boolean isTwoStepsForward = false;
                    if (i == 1 && isInStartPosition && target == null) {
                        Position intermediatePos = stepOrNull(this, new Direction[]{Direction.FORWARD}, start);
                        isTwoStepsForward = intermediatePos != null && boardMap.get(intermediatePos) == null;
                    }

                    // Diagonal capture - only if there's an enemy piece
                    boolean isDiagonalCapture = (i > 1 && target != null && target.getColour() != moverCol);

                    if (isOneStepForward || isTwoStepsForward || isDiagonalCapture) {
                        Log.d(TAG, String.format("Valid move to %s (type: %s)", end, 
                            isOneStepForward ? "one step" : 
                            isTwoStepsForward ? "two steps" : "capture"));
                        positionSet.add(end);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Error checking move: " + e.getMessage());
                }
            }
        }

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