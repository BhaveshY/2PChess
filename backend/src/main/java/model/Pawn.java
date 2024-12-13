package model;

import common.Colour;
import common.Direction;
import common.InvalidPositionException;
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
        BasePiece mover = this;
        Colour moverCol = mover.getColour();
        Direction[][] steps = this.directions;

        for (int i = 0; i < steps.length; i++) {
            Direction[] step = steps[i];
            Position end = stepOrNull(mover, step, start);

            if (end != null && !positionSet.contains(end)) {
                BasePiece target = boardMap.get(end);
                Log.d(TAG, "end: " + end + ", step: " + Arrays.toString(step));
                try {
                    boolean isOneStepForwardAndNotTakingPieceCase = (target == null && i == 0); // 1 step forward, not taking
                    boolean isTwoStepForwardAndNotTakingPieceCase = (target == null && i == 1 // 2 steps forward,
                            && start.getColour() == moverCol && start.getRow() == 1 // must be in initial position
                            && boardMap.get(Position.get(moverCol, 2, start.getColumn())) == null); // and can't jump a piece;
                    boolean isDiagonalMoveAndTakingPieceCase = (target != null && target.getColour() != moverCol && i > 1); // or taking diagonally

                    if (isOneStepForwardAndNotTakingPieceCase || isTwoStepForwardAndNotTakingPieceCase || isDiagonalMoveAndTakingPieceCase) {
                        Log.d(TAG, "position: " + end);
                        positionSet.add(end);
                    }
                } catch (InvalidPositionException e) {
                    Log.d(TAG, "InvalidPositionException: " + e.getMessage());
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