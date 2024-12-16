package entity;

import helper.Colour;
import helper.Direction;
import helper.Position;
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
     */
    public Pawn(Colour colour) {
        super(colour);
    }

    /**
     * Method to initialize directions for a chess piece
     */
    @Override
    public void setupDirections() {
        // Only set up basic forward movement, diagonal captures will be handled separately
        this.directions = new Direction[][] {
                {Direction.FORWARD}
        };
    }

    @Override
    public Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start) {
        Set<Position> positionSet = new HashSet<>();
        BasePiece mover = this;
        mover.getColour();

        // Handle forward movement
        addForwardMoves(positionSet, boardMap, start);
        
        // Handle diagonal captures
        addDiagonalCaptures(positionSet, boardMap, start);

        Log.d(TAG, String.format("Final possible moves: %s", positionSet));
        Log.d(TAG, "=== END PAWN MOVES ===\n");
        return positionSet;
    }

    /**
     * Add possible forward moves to the set
     */
    private void addForwardMoves(Set<Position> moves, Map<Position, BasePiece> boardMap, Position start) {
        try {
            // Check one step forward
            Position oneStep = stepOrNull(this, new Direction[]{Direction.FORWARD}, start);
            if (oneStep != null && boardMap.get(oneStep) == null) {
                moves.add(oneStep);

                // If pawn hasn't moved, check two steps forward
                if (isInStartingPosition(start)) {
                    Position twoStep = stepOrNull(this, new Direction[]{Direction.FORWARD, Direction.FORWARD}, start);
                    if (twoStep != null && boardMap.get(twoStep) == null) {
                        moves.add(twoStep);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Forward moves are off board");
        }
    }

    /**
     * Check if pawn is in its starting position
     */
    private boolean isInStartingPosition(Position position) {
        return position.getRow() == (getColour() == Colour.WHITE ? 6 : 1);
    }

    /**
     * Add possible diagonal capture moves to the set
     */
    private void addDiagonalCaptures(Set<Position> moves, Map<Position, BasePiece> boardMap, Position start) {
        addDiagonalCapture(moves, boardMap, start, Direction.LEFT);
        addDiagonalCapture(moves, boardMap, start, Direction.RIGHT);
    }

    /**
     * Add a single diagonal capture move if valid
     */
    private void addDiagonalCapture(Set<Position> moves, Map<Position, BasePiece> boardMap, 
                                  Position start, Direction direction) {
        try {
            Position capturePos = stepOrNull(this, new Direction[]{Direction.FORWARD, direction}, start);
            if (capturePos != null) {
                BasePiece target = boardMap.get(capturePos);
                Log.d(TAG, String.format("%s diagonal %s: %s", 
                    direction,
                    capturePos,
                    target != null ? target.getColour() + " " + target.getClass().getSimpleName() : "empty"));
                
                if (target != null && target.getColour() != getColour()) {
                    Log.d(TAG, "Can capture " + direction + " diagonal");
                    moves.add(capturePos);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, direction + " diagonal is off board");
        }
    }

    /**
     * Returns custom string representation of the class
     * @return String
     */
    @Override
    public String toString() {
        return this.colour.toString() + "P";
    }
}