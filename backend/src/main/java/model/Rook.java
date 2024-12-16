package model;

import helper.Colour;
import helper.Direction;
import helper.Position;
import utility.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static utility.MovementUtil.stepOrNull;

/**
 * Rook class extends BasePiece. Move directions for the Rook, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class Rook extends BasePiece {

    private static final String TAG = "ROOK";

    /**
     * Rook constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public Rook(Colour colour) {
        super(colour);
        setupDirections();
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    protected void setupDirections() {
        this.directions = new Direction[][] {
                {Direction.BACKWARD},
                {Direction.LEFT},
                {Direction.RIGHT},
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
        BasePiece mover = this;
        Direction[][] steps = this.directions;

        for (Direction[] step : steps) {
            Position tmp = stepOrNull(mover, step, start);
            while (tmp != null && !positionSet.contains(tmp) && (boardMap.get(tmp) == null || boardMap.get(tmp).getColour() != mover.getColour())) {
                Log.d(TAG, "tmp: " + tmp);
                positionSet.add(tmp);
                if (boardMap.get(tmp) != null && boardMap.get(tmp).getColour() != mover.getColour()) {
                    break;
                }
                tmp = stepOrNull(mover, step, tmp);
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
        return this.colour.toString() + "R";
    }
}