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
 * Knight class extends BasePiece. Move directions for the Knight, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class Knight extends BasePiece {

    private static final String TAG = "KNIGHT";

    /**
     * Knight constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public Knight(Colour colour) {
        super(colour);
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    protected void setupDirections() {
        this.directions = new Direction[][] {
                {Direction.FORWARD, Direction.FORWARD, Direction.LEFT},
                {Direction.FORWARD, Direction.FORWARD, Direction.RIGHT},
                {Direction.BACKWARD, Direction.BACKWARD, Direction.LEFT},
                {Direction.BACKWARD, Direction.BACKWARD, Direction.RIGHT},
                {Direction.LEFT, Direction.LEFT, Direction.FORWARD},
                {Direction.LEFT, Direction.LEFT, Direction.BACKWARD},
                {Direction.RIGHT, Direction.RIGHT, Direction.FORWARD},
                {Direction.RIGHT, Direction.RIGHT, Direction.BACKWARD}
        };
    }

    /**
     * Fetch all the possible positions where a piece can move on board
     * @param boardMap: Board class instance representing current game board
     * @param start: position of piece on board
     * @return Set of possible positions a piece is allowed to move
     * */
    @Override
    public Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start) {
        Set<Position> positionSet = new HashSet<>();
        BasePiece mover = this;
        Direction[][] steps = this.directions;

        for (Direction[] step : steps) {
            Position end = stepOrNull(mover, step, start);

            if (end != null) {
                BasePiece target = boardMap.get(end);

                if (target == null || target.getColour() != mover.getColour()) {
                    Log.d(TAG, "position: " + end);
                    positionSet.add(end);
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
        return this.colour.toString() + "N";
    }
}