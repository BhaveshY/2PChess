package entity;

import com.google.common.collect.ImmutableSet;
import helper.Colour;
import helper.InvalidMoveException;
import helper.InvalidPositionException;
import helper.Position;
import utility.BoardAdapter;
import utility.Log;
import utility.PieceFactory;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents the chess game board and manages game state.
 * 
 * <p>This class is responsible for:
 * <ul>
 *   <li>Board state management</li>
 *   <li>Piece placement and movement</li>
 *   <li>Move validation</li>
 *   <li>Game rules enforcement</li>
 *   <li>Game state tracking</li>
 * </ul>
 * 
 * <p>The board uses a dual color-space system where each physical square
 * can contain pieces from both WHITE and BLACK color spaces. This allows
 * for complex move validation and piece interaction in three-player chess.
 * 
 * @see BasePiece
 * @see Position
 * @see Colour
 * @version 1.0
 */
public class Board {

    /** Logger tag for this class */
    private static final String TAG = "Board";

    /** Maps positions to pieces on the board */
    protected Map<Position, BasePiece> boardMap;
    
    /** Current player's turn */
    private Colour turn;
    
    /** Flag indicating if the game has ended */
    private boolean gameOver;
    
    /** Color of the winning player */
    private String winner;
    
    /** Currently highlighted squares */
    private Set<Position> highlightPolygons = new HashSet<>();
    
    /** List of eliminated white pieces */
    private List<BasePiece> eliminatedWhitePieces = new ArrayList<>();
    
    /** List of eliminated black pieces */
    private List<BasePiece> eliminatedBlackPieces = new ArrayList<>();

    /**
     * Creates a new chess board with initial piece setup.
     * 
     * <p>Initializes:
     * <ul>
     *   <li>Empty board map</li>
     *   <li>White as starting player</li>
     *   <li>Places all pieces in starting positions</li>
     * </ul>
     */
    public Board() {
        boardMap = new HashMap<>();
        turn = Colour.WHITE;
        gameOver = false;
        winner = null;
        try {
            placeChessPieces(Colour.WHITE);
            placeChessPieces(Colour.BLACK);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "InvalidPositionException: " + e.getMessage());
        }
    }

    /**
     * Places all pieces for one color in their starting positions.
     * 
     * <p>Piece placement follows standard chess rules:
     * <ul>
     *   <li>Back row: Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook</li>
     *   <li>Front row: Eight pawns</li>
     * </ul>
     * 
     * @param colour Color of pieces to place
     * @throws InvalidPositionException if a position is invalid
     */
    private void placeChessPieces(Colour colour) throws InvalidPositionException {
        int pawnRow = colour == Colour.WHITE ? 6 : 1;
        int mainRow = colour == Colour.WHITE ? 7 : 0;

        // Place pieces in standard chess formation
        boardMap.put(Position.get(colour, mainRow, 0), PieceFactory.createPiece("Rook", colour));
        boardMap.put(Position.get(colour, mainRow, 7), PieceFactory.createPiece("Rook", colour));
        boardMap.put(Position.get(colour, mainRow, 1), PieceFactory.createPiece("Knight", colour));
        boardMap.put(Position.get(colour, mainRow, 6), PieceFactory.createPiece("Knight", colour));
        boardMap.put(Position.get(colour, mainRow, 2), PieceFactory.createPiece("Bishop", colour));
        boardMap.put(Position.get(colour, mainRow, 5), PieceFactory.createPiece("Bishop", colour));
        boardMap.put(Position.get(colour, mainRow, 3), PieceFactory.createPiece("Queen", colour));
        boardMap.put(Position.get(colour, mainRow, 4), PieceFactory.createPiece("King", colour));

        // Place pawns
        for (int i = 0; i < 8; i++) {
            boardMap.put(Position.get(colour, pawnRow, i), PieceFactory.createPiece("Pawn", colour));
        }
    }

    /**
     * Checks if the game has ended.
     * 
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the color of the winning player.
     * 
     * @return Color of winner, or null if game is not over
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Executes a move on the board.
     * 
     * <p>This method:
     * <ul>
     *   <li>Validates the move</li>
     *   <li>Handles piece capture</li>
     *   <li>Updates board state</li>
     *   <li>Handles special moves (castling, promotion)</li>
     *   <li>Checks for game end conditions</li>
     * </ul>
     * 
     * @param start Starting position
     * @param end Target position
     * @throws InvalidMoveException if the move is illegal
     * @throws InvalidPositionException if a position is invalid
     */
    public void move(Position start, Position end) throws InvalidMoveException, InvalidPositionException {
        logMoveAttempt(start, end);
        
        BasePiece mover = validateMoveStart(start);
        Position targetPos = getTargetPosition(mover, end);
        BasePiece targetPiece = boardMap.get(targetPos);
        
        logMoveDetails(mover, targetPiece, targetPos);

        if (isLegalMove(start, end)) {
            executeMove(start, end, mover, targetPos, targetPiece);
            handleSpecialMoves(start, end, mover);
            checkGameEnd(mover);
            advanceTurn();
        } else {
            logIllegalMove(mover, start, end);
            throw new InvalidMoveException(
                String.format("Illegal Move: %s-%s for piece %s", 
                start, end, mover.getClass().getSimpleName()));
        }
    }

    /**
     * Validates if a move is legal according to chess rules.
     * 
     * <p>Checks:
     * <ul>
     *   <li>Piece movement rules</li>
     *   <li>Turn order</li>
     *   <li>Check conditions</li>
     *   <li>Board boundaries</li>
     * </ul>
     * 
     * @param start Starting position
     * @param end Target position
     * @return true if move is legal, false otherwise
     */
    public boolean isLegalMove(Position start, Position end) {
        try {
            BasePiece mover = boardMap.get(start);
            if (!isValidMover(mover)) {
                return false;
            }

            Position targetPos = getTargetPosition(mover, end);
            BasePiece targetPiece = boardMap.get(targetPos);
            
            if (!isValidTarget(mover, targetPiece)) {
                return false;
            }

            if (!isPossibleMove(mover, start, end)) {
                return false;
            }

            return !wouldResultInCheck(start, end, targetPos, targetPiece, mover);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Invalid position in isLegalMove: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the current player's turn.
     * 
     * @return Color of current player
     */
    public Colour getTurn() {
        return turn;
    }

    /**
     * Gets the piece at a specific position.
     * 
     * @param position Board position to check
     * @return Piece at position, or null if empty
     */
    public BasePiece getPiece(Position position) {
        return boardMap.get(position);
    }

    /**
     * Gets the board representation for web view.
     * 
     * @return Map of positions to piece representations
     */
    public Map<String, String> getWebViewBoard() {
        return BoardAdapter.convertModelBoardToViewBoard(this.boardMap);
    }

    /**
     * Gets all possible moves for a piece at a position.
     * 
     * <p>This method:
     * <ul>
     *   <li>Gets basic moves from piece</li>
     *   <li>Filters out moves that would result in check</li>
     *   <li>Handles special moves</li>
     * </ul>
     * 
     * @param position Position of piece
     * @return Set of valid target positions
     */
    public Set<Position> getPossibleMoves(Position position) {
        BasePiece mover = boardMap.get(position);
        if (mover == null) {
            return ImmutableSet.of();
        }

        highlightPolygons = mover.getPossibleMoves(this.boardMap, position);
        return filterCheckMoves(position, mover);
    }

    /**
     * Checks if a position contains current player's piece.
     * 
     * @param position Position to check
     * @return true if position has current player's piece
     */
    public boolean isCurrentPlayersPiece(Position position) {
        return getPiece(position) != null && getPiece(position).getColour() == turn;
    }

    /**
     * Gets lists of eliminated pieces for each color.
     * 
     * @return Map of color to list of eliminated pieces
     */
    public Map<String, List<String>> getEliminatedPieces() {
        Map<String, List<String>> eliminatedPieces = new HashMap<>();
        
        eliminatedPieces.put("white", 
            eliminatedWhitePieces.stream()
                .map(BasePiece::toString)
                .collect(Collectors.toList()));
        
        eliminatedPieces.put("black", 
            eliminatedBlackPieces.stream()
                .map(BasePiece::toString)
                .collect(Collectors.toList()));
        
        return eliminatedPieces;
    }

    /**
     * Gets the current board state map.
     * 
     * @return Map of positions to pieces
     */
    public Map<Position, BasePiece> getBoardMap() {
        return boardMap;
    }

    // Private helper methods...
    private void logMoveAttempt(Position start, Position end) {
        Log.d(TAG, "\n=== MOVE ATTEMPT ===");
        Log.d(TAG, String.format("Start position: %s (row: %d, col: %d)", 
            start, start.getRow(), start.getColumn()));
        Log.d(TAG, String.format("End position: %s (row: %d, col: %d)", 
            end, end.getRow(), end.getColumn()));
    }

    private BasePiece validateMoveStart(Position start) throws InvalidMoveException {
        BasePiece mover = boardMap.get(start);
        if (mover == null) {
            Log.e(TAG, "ERROR: No piece at start position");
            throw new InvalidMoveException("No piece at start position");
        }
        return mover;
    }

    private Position getTargetPosition(BasePiece mover, Position end) throws InvalidPositionException {
        return Position.get(
            end.getColour() == mover.getColour() ? 
                (mover.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE) 
                : end.getColour(),
            end.getRow(), end.getColumn());
    }

    private void logMoveDetails(BasePiece mover, BasePiece targetPiece, Position targetPos) {
        if (targetPiece != null) {
            Log.d(TAG, String.format("Target square contains: %s %s", 
                targetPiece.getColour(), targetPiece.getClass().getSimpleName()));
        } else {
            Log.d(TAG, "Target square is empty");
        }

        Log.d(TAG, String.format("Moving piece: %s, Color: %s, Current turn: %s", 
            mover.getClass().getSimpleName(), mover.getColour(), turn));
    }

    private void executeMove(Position start, Position end, BasePiece mover, 
            Position targetPos, BasePiece targetPiece) {
        handleCapture(targetPiece, targetPos);
        updateBoardState(start, end, mover);
    }

    private void handleCapture(BasePiece targetPiece, Position targetPos) {
        if (targetPiece != null) {
            if (targetPiece.getColour() == Colour.WHITE) {
                eliminatedWhitePieces.add(targetPiece);
            } else {
                eliminatedBlackPieces.add(targetPiece);
            }
            boardMap.remove(targetPos);
        }
    }

    private void updateBoardState(Position start, Position end, BasePiece mover) {
        boardMap.remove(start);
        Position finalPos = null;
        try {
            finalPos = Position.get(mover.getColour(), end.getRow(), end.getColumn());
            if (shouldPromotePawn(mover, end)) {
                boardMap.put(finalPos, createPromotedPiece(mover));
            } else {
                boardMap.put(finalPos, mover);
            }
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error updating board state: " + e.getMessage());
        }
    }

    /**
     * Check if a piece should be promoted (pawn reaching opposite end)
     */
    private boolean shouldPromotePawn(BasePiece piece, Position position) {
        return piece instanceof Pawn && isPromotionRank(piece.getColour(), position.getRow());
    }

    /**
     * Check if a row is the promotion rank for a given color
     */
    private boolean isPromotionRank(Colour colour, int row) {
        return row == (colour == Colour.WHITE ? 0 : 7);
    }

    /**
     * Create a promoted piece (currently always promotes to Queen)
     */
    private BasePiece createPromotedPiece(BasePiece original) {
        return new Queen(original.getColour());
    }

    private void handleSpecialMoves(Position start, Position end, BasePiece mover) {
        if (mover instanceof King && start.getColumn() == 4) {
            handleCastling(start, end, mover);
        }
    }

    private void handleCastling(Position start, Position end, BasePiece mover) {
        try {
            if (end.getColumn() == 2) {
                Position rookPos = Position.get(mover.getColour(), start.getRow(), 0);
                boardMap.put(Position.get(mover.getColour(), start.getRow(), 3), boardMap.get(rookPos));
                boardMap.remove(rookPos);
            } else if (end.getColumn() == 6) {
                Position rookPos = Position.get(mover.getColour(), start.getRow(), 7);
                boardMap.put(Position.get(mover.getColour(), start.getRow(), 5), boardMap.get(rookPos));
                boardMap.remove(rookPos);
            }
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error handling castling: " + e.getMessage());
        }
    }

    private void checkGameEnd(BasePiece mover) {
        Colour nextTurn = turn.next();
        if (isCheckMate(nextTurn, boardMap)) {
            gameOver = true;
            winner = mover.getColour().toString();
        }
    }

    private void advanceTurn() {
        turn = turn.next();
    }

    private void logIllegalMove(BasePiece mover, Position start, Position end) {
        Log.e(TAG, "\nILLEGAL MOVE DETAILS:");
        Log.e(TAG, String.format("- Piece: %s", mover.getClass().getSimpleName()));
        Log.e(TAG, String.format("- Color: %s", mover.getColour()));
        Log.e(TAG, String.format("- Current turn: %s", turn));
        Log.e(TAG, String.format("- Start: %s", start));
        Log.e(TAG, String.format("- End: %s", end));
        Set<Position> possibleMoves = mover.getPossibleMoves(this.boardMap, start);
        Log.e(TAG, String.format("- Possible moves: %s", possibleMoves));
    }

    private boolean isValidMover(BasePiece mover) {
        return mover != null && mover.getColour() == turn;
    }

    private boolean isValidTarget(BasePiece mover, BasePiece targetPiece) {
        return targetPiece == null || targetPiece.getColour() != mover.getColour();
    }

    private boolean isPossibleMove(BasePiece mover, Position start, Position end) {
        Set<Position> possibleMoves = mover.getPossibleMoves(this.boardMap, start);
        return possibleMoves.contains(end);
    }

    private boolean wouldResultInCheck(Position start, Position end, 
            Position targetPos, BasePiece targetPiece, BasePiece mover) {
        Map<Position, BasePiece> testBoardMap = new HashMap<>(boardMap);
        testBoardMap.remove(start);
        if (targetPiece != null) {
            testBoardMap.remove(targetPos);
        }
        try {
            Position finalPos = Position.get(mover.getColour(), end.getRow(), end.getColumn());
            testBoardMap.put(finalPos, mover);
            return isCheck(turn, testBoardMap);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Error checking for check: " + e.getMessage());
            return true;
        }
    }

    private Set<Position> filterCheckMoves(Position position, BasePiece mover) {
        Set<Position> nonCheckPositions = new HashSet<>();
        for (Position endPos : highlightPolygons) {
            try {
                Position targetPos = getTargetPosition(mover, endPos);
                BasePiece targetPiece = boardMap.get(targetPos);

                Map<Position, BasePiece> testBoardMap = new HashMap<>(boardMap);
                testBoardMap.remove(position);
                if (targetPiece != null) {
                    testBoardMap.remove(targetPos);
                }
                Position finalPos = Position.get(mover.getColour(), endPos.getRow(), endPos.getColumn());
                testBoardMap.put(finalPos, mover);

                if (!isCheck(mover.getColour(), testBoardMap)) {
                    nonCheckPositions.add(endPos);
                }
            } catch (InvalidPositionException e) {
                Log.e(TAG, "Invalid position in getPossibleMoves: " + e.getMessage());
            }
        }
        return nonCheckPositions;
    }

    private boolean isCheck(Colour colour, Map<Position, BasePiece> boardMap) {
        Position kingPosition = getKingPosition(colour, boardMap);
        if (kingPosition == null) {
            return false;
        }

        for (Map.Entry<Position, BasePiece> entry : boardMap.entrySet()) {
            BasePiece piece = entry.getValue();
            if (piece.getColour() != colour) {
                Set<Position> moves = piece.getPossibleMoves(boardMap, entry.getKey());
                if (moves.contains(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCheckMate(Colour colour, Map<Position, BasePiece> boardMap) {
        if (!isCheck(colour, boardMap)) {
            return false;
        }

        for (Position position : boardMap.keySet()) {
            BasePiece piece = boardMap.get(position);
            if (piece.getColour() == colour) {
                Set<Position> possibleMoves = piece.getPossibleMoves(boardMap, position);
                for (Position endPos : possibleMoves) {
                    if (!isCheckAfterLegalMove(colour, boardMap, position, endPos)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isCheckAfterLegalMove(Colour colour, Map<Position, BasePiece> boardMap, 
            Position start, Position end) {
        try {
            BasePiece mover = boardMap.get(start);
            Position targetPos = getTargetPosition(mover, end);
            BasePiece targetPiece = boardMap.get(targetPos);

            Map<Position, BasePiece> testBoardMap = new HashMap<>(boardMap);
            testBoardMap.remove(start);
            if (targetPiece != null) {
                testBoardMap.remove(targetPos);
            }
            Position finalPos = Position.get(mover.getColour(), end.getRow(), end.getColumn());
            testBoardMap.put(finalPos, mover);

            return isCheck(colour, testBoardMap);
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Invalid position in isCheckAfterLegalMove: " + e.getMessage());
            return true;
        }
    }

    private Position getKingPosition(Colour colour, Map<Position, BasePiece> boardMap) {
        for (Position position : boardMap.keySet()) {
            BasePiece piece = boardMap.get(position);
            if (piece instanceof King && piece.getColour() == colour) {
                return position;
            }
        }
        return null;
    }
}
