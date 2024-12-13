package model;

import common.Colour;
import common.Direction;
import common.InvalidPositionException;
import common.Position;
import utility.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static utility.MovementUtil.stepOrNull;

/**
 * King class extends BasePiece. Move directions for the King, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class King extends BasePiece {

    public static final String TAG = "KING";

    public Map<Colour, List<Position>> castlingPositionMapping;

    /**
     * King constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public King(Colour colour) {
        super(colour);
        castlingPositionMapping = new HashMap<>();

        try {
            for (Colour c : Colour.values()) {
                List<Position> castlingPositions = castlingPositionMapping.getOrDefault(c, new ArrayList<>());
                castlingPositions.add(Position.get(c, 0, 6));
                castlingPositions.add(Position.get(c, 0, 2));
                castlingPositionMapping.put(c, castlingPositions);
            }
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Exception while adding castling end position: " + e.getMessage());
        }
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    protected void setupDirections() {
        this.directions = new Direction[][] {
                {Direction.FORWARD, Direction.LEFT},
                {Direction.FORWARD, Direction.RIGHT},
                {Direction.LEFT, Direction.FORWARD},
                {Direction.RIGHT, Direction.FORWARD},
                {Direction.BACKWARD, Direction.LEFT},
                {Direction.BACKWARD, Direction.RIGHT},
                {Direction.LEFT, Direction.BACKWARD},
                {Direction.RIGHT, Direction.BACKWARD},
                {Direction.FORWARD},
                {Direction.BACKWARD},
                {Direction.LEFT},
                {Direction.RIGHT}
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
            Position end = stepOrNull(mover, step, start);
            if (end != null && !positionSet.contains(end) && (boardMap.get(end) == null || boardMap.get(end).getColour() != mover.getColour())) {
                Log.d(TAG, "position: " + end);
                positionSet.add(end);
            }
        }

        List<Position> castlingPositions = castlingPositionMapping.getOrDefault(mover.getColour(), new ArrayList<>());
        for (Position end : castlingPositions) {
            if (boardMap.get(end) == null && isCastlingPossible(boardMap, start, end)) {
                Log.d(TAG, "position castling: " + end);
                positionSet.add(end);
            }
        }

        return positionSet;
    }

    /**
     * Method to check if castling is possible between given positions
     * @param board: Board class instance representing current game board
     * @param start: start position of piece on board
     * @param end: start position of piece on board
     * @return bool if castling is possible
     * */
    private boolean isCastlingPossible(Map<Position, BasePiece> board, Position start, Position end) {
        Log.d(TAG, "isCastlingPossible: start: " + start + ", end: " + end);
        BasePiece mover = this;
        Colour moverCol = mover.getColour();
        try {
            if (start.equals(Position.get(moverCol, 0, 4))) {
                if (end.equals(Position.get(moverCol, 0, 6))) {
                    BasePiece castle = board.get(Position.get(moverCol, 0, 7));
                    BasePiece empty1 = board.get(Position.get(moverCol, 0, 5));
                    BasePiece empty2 = board.get(Position.get(moverCol, 0, 6));
                    if (castle instanceof Rook && castle.getColour() == mover.getColour()
                            && empty1 == null && empty2 == null) {
                        Log.d(TAG, "Castling Legal Move 1: True");
                        return true;
                    }
                }
                if (end.equals(Position.get(moverCol, 0, 2))) {
                    BasePiece castle = board.get(Position.get(moverCol, 0, 0));
                    BasePiece empty1 = board.get(Position.get(moverCol, 0, 1));
                    BasePiece empty2 = board.get(Position.get(moverCol, 0, 2));
                    BasePiece empty3 = board.get(Position.get(moverCol, 0, 3));
                    if (castle instanceof Rook && castle.getColour() == mover.getColour()
                            && empty1 == null && empty2 == null && empty3 == null) {
                        Log.d(TAG, "Castling Legal Move 2: True");
                        return true;
                    }
                }
            }
        } catch (InvalidPositionException e) {
            // do nothing, steps went off board.
            Log.e(TAG, "InvalidPositionException: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns custom string representation of the class
     * @return String
     * */
    @Override
    public String toString() {
        return this.colour.toString() + "K";
    }
}