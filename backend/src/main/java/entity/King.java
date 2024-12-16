package entity;

import helper.Colour;
import helper.Direction;
import helper.InvalidPositionException;
import helper.Position;
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
        initializeCastlingPositions();
    }

    /**
     * Initialize valid castling end positions for each color
     */
    private void initializeCastlingPositions() {
        castlingPositionMapping = new HashMap<>();
        try {
            for (Colour c : Colour.values()) {
                List<Position> castlingPositions = castlingPositionMapping.getOrDefault(c, new ArrayList<>());
                castlingPositions.add(Position.get(c, 0, 6)); // King-side castling
                castlingPositions.add(Position.get(c, 0, 2)); // Queen-side castling
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
     * Method to check if castling is possible between given positions
     * @param board: Board class instance representing current game board
     * @param start: start position of piece on board
     * @param end: end position of piece on board
     * @return bool if castling is possible
     */
    private boolean isCastlingPossible(Map<Position, BasePiece> board, Position start, Position end) {
        Log.d(TAG, "isCastlingPossible: start: " + start + ", end: " + end);
        
        if (!isKingInStartPosition(start)) {
            return false;
        }
        
        return isKingSideCastlingPossible(board, end) || isQueenSideCastlingPossible(board, end);
    }

    /**
     * Check if king is in its initial position
     */
    private boolean isKingInStartPosition(Position start) {
        try {
            return start.equals(Position.get(getColour(), 0, 4));
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error checking king position: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if king-side castling is possible
     */
    private boolean isKingSideCastlingPossible(Map<Position, BasePiece> board, Position end) {
        try {
            if (!end.equals(Position.get(getColour(), 0, 6))) {
                return false;
            }
            
            Position rookPos = Position.get(getColour(), 0, 7);
            Position empty1Pos = Position.get(getColour(), 0, 5);
            Position empty2Pos = Position.get(getColour(), 0, 6);
            
            return isCastlingPathClear(board, rookPos, empty1Pos, empty2Pos);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error checking king-side castling: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if queen-side castling is possible
     */
    private boolean isQueenSideCastlingPossible(Map<Position, BasePiece> board, Position end) {
        try {
            if (!end.equals(Position.get(getColour(), 0, 2))) {
                return false;
            }
            
            Position rookPos = Position.get(getColour(), 0, 0);
            Position empty1Pos = Position.get(getColour(), 0, 1);
            Position empty2Pos = Position.get(getColour(), 0, 2);
            Position empty3Pos = Position.get(getColour(), 0, 3);
            
            return isCastlingPathClear(board, rookPos, empty1Pos, empty2Pos, empty3Pos);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error checking queen-side castling: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if the path for castling is clear and rook is present
     */
    private boolean isCastlingPathClear(Map<Position, BasePiece> board, Position rookPos, Position... emptyPositions) {
        BasePiece rook = board.get(rookPos);
        if (!(rook instanceof Rook) || rook.getColour() != getColour()) {
            return false;
        }
        
        for (Position pos : emptyPositions) {
            if (board.get(pos) != null) {
                return false;
            }
        }
        
        return true;
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
     * Returns custom string representation of the class
     * @return String
     */
    @Override
    public String toString() {
        return this.colour.toString() + "K";
    }
}