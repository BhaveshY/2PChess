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

public class Board {

    private static final String TAG = "Board";

    protected Map<Position, BasePiece> boardMap;
    private Colour turn;
    private boolean gameOver;
    private String winner;
    private Set<Position> highlightPolygons = new HashSet<>();
    private List<BasePiece> eliminatedWhitePieces = new ArrayList<>();
    private List<BasePiece> eliminatedBlackPieces = new ArrayList<>();

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

    private void placeChessPieces(Colour colour) throws InvalidPositionException {
        // White pieces start on rows 6-7 (bottom)
        // Black pieces start on rows 0-1 (top)
        int pawnRow = colour == Colour.WHITE ? 6 : 1;
        int mainRow = colour == Colour.WHITE ? 7 : 0;

        // place ROOK
        boardMap.put(Position.get(colour, mainRow, 0), PieceFactory.createPiece("Rook", colour));
        boardMap.put(Position.get(colour, mainRow, 7), PieceFactory.createPiece("Rook", colour));

        // place KNIGHT
        boardMap.put(Position.get(colour, mainRow, 1), PieceFactory.createPiece("Knight", colour));
        boardMap.put(Position.get(colour, mainRow, 6), PieceFactory.createPiece("Knight", colour));

        // place BISHOP
        boardMap.put(Position.get(colour, mainRow, 2), PieceFactory.createPiece("Bishop", colour));
        boardMap.put(Position.get(colour, mainRow, 5), PieceFactory.createPiece("Bishop", colour));

        // place QUEEN
        boardMap.put(Position.get(colour, mainRow, 3), PieceFactory.createPiece("Queen", colour));

        // place KING
        boardMap.put(Position.get(colour, mainRow, 4), PieceFactory.createPiece("King", colour));

        // place PAWN
        for (int i = 0; i < 8; i++) {
            boardMap.put(Position.get(colour, pawnRow, i), PieceFactory.createPiece("Pawn", colour));
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void move(Position start, Position end) throws InvalidMoveException, InvalidPositionException {
        Log.d(TAG, "\n=== MOVE ATTEMPT ===");
        Log.d(TAG, String.format("Start position: %s (row: %d, col: %d)", start, start.getRow(), start.getColumn()));
        Log.d(TAG, String.format("End position: %s (row: %d, col: %d)", end, end.getRow(), end.getColumn()));

        BasePiece mover = boardMap.get(start);
        if (mover == null) {
            Log.e(TAG, "ERROR: No piece at start position");
            throw new InvalidMoveException("No piece at start position");
        }

        // Log the current board state
        Log.d(TAG, "\nCurrent board state:");
        for (Map.Entry<Position, BasePiece> entry : boardMap.entrySet()) {
            Log.d(TAG, String.format("Position %s: %s %s", 
                entry.getKey(), entry.getValue().getColour(), entry.getValue().getClass().getSimpleName()));
        }

        // Check if there's a piece at the target position
        Position targetPos = Position.get(end.getColour() == mover.getColour() ? 
            (mover.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE) : end.getColour(), 
            end.getRow(), end.getColumn());
        BasePiece targetPiece = boardMap.get(targetPos);
        
        if (targetPiece != null) {
            Log.d(TAG, String.format("Target square contains: %s %s", 
                targetPiece.getColour(), targetPiece.getClass().getSimpleName()));
        } else {
            Log.d(TAG, "Target square is empty");
        }

        Log.d(TAG, String.format("Moving piece: %s, Color: %s, Current turn: %s", 
            mover.getClass().getSimpleName(), mover.getColour(), turn));

        if (isLegalMove(start, end)) {
            // If there's a piece at the target position, it will be captured
            if (targetPiece != null) {
                Log.d(TAG, String.format("Capturing piece %s %s at %s", 
                    targetPiece.getColour(), targetPiece.getClass().getSimpleName(), targetPos));
                
                // Add captured piece to eliminated pieces list
                if (targetPiece.getColour() == Colour.WHITE) {
                    eliminatedWhitePieces.add(targetPiece);
                } else {
                    eliminatedBlackPieces.add(targetPiece);
                }
                // Remove captured piece from board
                boardMap.remove(targetPos);
                Log.d(TAG, String.format("Removed captured piece from %s", targetPos));
            }

            // Remove piece from start position
            boardMap.remove(start);
            Log.d(TAG, String.format("Removed piece from %s", start));

            // Place piece at end position
            Position finalPos = Position.get(mover.getColour(), end.getRow(), end.getColumn());
            if (mover instanceof Pawn && end.getRow() == (mover.getColour() == Colour.WHITE ? 0 : 7)) {
                boardMap.put(finalPos, new Queen(mover.getColour()));
                Log.d(TAG, "Pawn promoted to Queen");
            } else {
                boardMap.put(finalPos, mover);
                Log.d(TAG, String.format("Placed %s at %s", mover.getClass().getSimpleName(), finalPos));
            }

            // Log the new board state
            Log.d(TAG, "\nNew board state:");
            for (Map.Entry<Position, BasePiece> entry : boardMap.entrySet()) {
                Log.d(TAG, String.format("Position %s: %s %s", 
                    entry.getKey(), entry.getValue().getColour(), entry.getValue().getClass().getSimpleName()));
            }

            // Handle castling
            if (mover instanceof King && start.getColumn() == 4) {
                if (end.getColumn() == 2) {
                    Position rookPos = Position.get(mover.getColour(), start.getRow(), 0);
                    boardMap.put(Position.get(mover.getColour(), start.getRow(), 3), boardMap.get(rookPos));
                    boardMap.remove(rookPos);
                    Log.d(TAG, "Kingside castling performed");
                } else if (end.getColumn() == 6) {
                    Position rookPos = Position.get(mover.getColour(), start.getRow(), 7);
                    boardMap.put(Position.get(mover.getColour(), start.getRow(), 5), boardMap.get(rookPos));
                    boardMap.remove(rookPos);
                    Log.d(TAG, "Queenside castling performed");
                }
            }

            // Check for checkmate
            Colour nextTurn = turn.next();
            if (isCheckMate(nextTurn, boardMap)) {
                gameOver = true;
                winner = mover.getColour().toString();
                Log.d(TAG, String.format("Checkmate! Winner: %s", winner));
            }

            // Update turn
            turn = nextTurn;
            Log.d(TAG, String.format("Turn updated to: %s", turn));
            Log.d(TAG, "=== MOVE COMPLETED ===\n");
        } else {
            Log.e(TAG, "\nILLEGAL MOVE DETAILS:");
            Log.e(TAG, String.format("- Piece: %s", mover.getClass().getSimpleName()));
            Log.e(TAG, String.format("- Color: %s", mover.getColour()));
            Log.e(TAG, String.format("- Current turn: %s", turn));
            Log.e(TAG, String.format("- Start: %s", start));
            Log.e(TAG, String.format("- End: %s", end));
            Set<Position> possibleMoves = mover.getPossibleMoves(this.boardMap, start);
            Log.e(TAG, String.format("- Possible moves: %s", possibleMoves));
            throw new InvalidMoveException(String.format("Illegal Move: %s-%s for piece %s", start, end, mover.getClass().getSimpleName()));
        }
    }

    public boolean isLegalMove(Position start, Position end) {
        BasePiece mover = getPiece(start);
        if (mover == null || mover.getColour() != turn) {
            Log.d(TAG, String.format("Illegal move: piece is null or wrong color. Piece: %s, Turn: %s", 
                mover != null ? mover.getColour() : "null", turn));
            return false;
        }

        try {
            // Get target piece from both color spaces
            Position targetPos = Position.get(end.getColour() == mover.getColour() ? 
                (mover.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE) : end.getColour(), 
                end.getRow(), end.getColumn());
            BasePiece targetPiece = boardMap.get(targetPos);
            
            // Check if target piece exists and is of the same color
            if (targetPiece != null && targetPiece.getColour() == mover.getColour()) {
                Log.d(TAG, String.format("Illegal move: cannot capture own piece at %s", targetPos));
                return false;
            }

            Set<Position> possibleMoves = mover.getPossibleMoves(this.boardMap, start);
            if (!possibleMoves.contains(end)) {
                Log.d(TAG, String.format("Illegal move: end position %s not in possible moves for piece at %s", end, start));
                return false;
            }

            // Check if move would put/leave king in check
            Map<Position, BasePiece> testBoardMap = new HashMap<>(boardMap);
            testBoardMap.remove(start);
            if (targetPiece != null) {
                testBoardMap.remove(targetPos);
            }
            Position finalPos = Position.get(mover.getColour(), end.getRow(), end.getColumn());
            testBoardMap.put(finalPos, mover);

            if (isCheck(turn, testBoardMap)) {
                Log.d(TAG, String.format("Illegal move: piece at %s to %s would put/leave king in check", start, end));
                return false;
            }

            return true;
        } catch (InvalidPositionException e) {
            Log.e(TAG, "Invalid position in isLegalMove: " + e.getMessage());
            return false;
        }
    }

    public Colour getTurn() {
        return turn;
    }

    public BasePiece getPiece(Position position) {
        return boardMap.get(position);
    }

    public Map<String, String> getWebViewBoard() {
        return BoardAdapter.convertModelBoardToViewBoard(this.boardMap);
    }

    public Set<Position> getPossibleMoves(Position position) {
        BasePiece mover = boardMap.get(position);
        if (mover == null) {
            return ImmutableSet.of();
        }
        highlightPolygons = mover.getPossibleMoves(this.boardMap, position);

        Colour moverColour = mover.getColour();
        Set<Position> nonCheckPositions = new HashSet<>();
        for (Position endPos : highlightPolygons) {
            try {
                // Get target piece from both color spaces
                Position targetPos = Position.get(endPos.getColour() == moverColour ? 
                    (moverColour == Colour.WHITE ? Colour.BLACK : Colour.WHITE) : endPos.getColour(), 
                    endPos.getRow(), endPos.getColumn());
                BasePiece targetPiece = boardMap.get(targetPos);

                // Create test board state
                Map<Position, BasePiece> testBoardMap = new HashMap<>(boardMap);
                testBoardMap.remove(position);
                if (targetPiece != null) {
                    testBoardMap.remove(targetPos);
                }
                Position finalPos = Position.get(moverColour, endPos.getRow(), endPos.getColumn());
                testBoardMap.put(finalPos, mover);

                if (!isCheck(moverColour, testBoardMap)) {
                    nonCheckPositions.add(endPos);
                }
            } catch (InvalidPositionException e) {
                Log.e(TAG, "Invalid position in getPossibleMoves: " + e.getMessage());
                // Skip this position if it's invalid
                continue;
            }
        }

        return nonCheckPositions;
    }

    public boolean isCurrentPlayersPiece(Position position) {
        return getPiece(position) != null && getPiece(position).getColour() == turn;
    }

    private boolean isCheck(Colour colour, Map<Position, BasePiece> boardMap) {
        Position kingPosition = getKingPosition(colour, boardMap);

        for (Position position : boardMap.keySet()) {
            BasePiece piece = boardMap.get(position);
            if (piece.getColour() != colour) {
                Set<Position> possibleTargetPositions = piece.getPossibleMoves(boardMap, position);
                if (possibleTargetPositions.contains(kingPosition)) {
                    Log.d(TAG, "Piece " + piece + " is attacking King of colour " + colour);
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
                        Log.d(TAG, "Piece " + piece + " can help colour " + colour + " to come out of check: st: " + position + ", end: " + endPos);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isCheckAfterLegalMove(Colour colour, Map<Position, BasePiece> boardMap, Position start, Position end) {
        try {
            BasePiece mover = boardMap.get(start);
            Position targetPos = Position.get(end.getColour() == mover.getColour() ? 
                (mover.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE) : end.getColour(), 
                end.getRow(), end.getColumn());
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
            // If we can't validate the move properly, assume it would result in check
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

    public Map<String, List<String>> getEliminatedPieces() {
        Map<String, List<String>> eliminatedPieces = new HashMap<>();
        
        List<String> whitePieces = eliminatedWhitePieces.stream()
            .map(BasePiece::toString)
            .collect(Collectors.toList());
        
        List<String> blackPieces = eliminatedBlackPieces.stream()
            .map(BasePiece::toString)
            .collect(Collectors.toList());
        
        eliminatedPieces.put("white", whitePieces);
        eliminatedPieces.put("black", blackPieces);
        
        return eliminatedPieces;
    }

    public Map<Position, BasePiece> getBoardMap() {
        return boardMap;
    }
}
