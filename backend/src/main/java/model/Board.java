package model;

import com.google.common.collect.ImmutableSet;
import common.Colour;
import common.InvalidMoveException;
import common.InvalidPositionException;
import common.Position;
import utility.BoardAdapter;
import utility.Log;
import utility.PieceFactory;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class Board {

    private static final String TAG = "Board";

    protected Map<Position, BasePiece> boardMap;
    private Colour turn;
    private boolean gameOver;
    private String winner;
    private Set<Position> highlightPolygons = new HashSet<>();

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
        Log.d(TAG, String.format("Attempting move from %s to %s", start, end));
        BasePiece mover = boardMap.get(start);
        if (mover == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        Log.d(TAG, String.format("Moving piece %s of color %s", mover.getClass().getSimpleName(), mover.getColour()));

        if (isLegalMove(start, end)) {
            // Check if there's a piece to capture at the end position
            BasePiece capturedPiece = boardMap.get(end);
            if (capturedPiece != null) {
                Log.d(TAG, String.format("Capturing piece %s of color %s", 
                    capturedPiece.getClass().getSimpleName(), capturedPiece.getColour()));
            }

            // Remove piece from start position
            boardMap.remove(start);
            
            // Place piece at end position (this will automatically remove any captured piece)
            if (mover instanceof Pawn && end.getRow() == (mover.getColour() == Colour.WHITE ? 0 : 7)) {
                boardMap.put(end, new Queen(mover.getColour()));
            } else {
                boardMap.put(end, mover);
            }

            // Handle castling
            if (mover instanceof King && start.getColumn() == 4) {
                if (end.getColumn() == 2) {
                    Position rookPos = Position.get(mover.getColour(), start.getRow(), 0);
                    boardMap.put(Position.get(mover.getColour(), start.getRow(), 3), boardMap.get(rookPos));
                    boardMap.remove(rookPos);
                } else if (end.getColumn() == 6) {
                    Position rookPos = Position.get(mover.getColour(), start.getRow(), 7);
                    boardMap.put(Position.get(mover.getColour(), start.getRow(), 5), boardMap.get(rookPos));
                    boardMap.remove(rookPos);
                }
            }

            // Check for checkmate
            Colour nextTurn = turn.next();
            if (isCheckMate(nextTurn, boardMap)) {
                gameOver = true;
                winner = mover.getColour().toString();
            }

            // Update turn
            turn = nextTurn;
            Log.d(TAG, String.format("Move completed. New turn: %s", turn));
        } else {
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

        Set<Position> possibleMoves = mover.getPossibleMoves(this.boardMap, start);
        if (!possibleMoves.contains(end)) {
            Log.d(TAG, String.format("Illegal move: end position %s not in possible moves for piece at %s", end, start));
            return false;
        }

        // Check if move would put/leave king in check
        if (isCheck(turn, boardMap) && isCheckAfterLegalMove(turn, boardMap, start, end)) {
            Log.d(TAG, String.format("Illegal move: piece at %s to %s would leave king in check", start, end));
            return false;
        } else if (isCheckAfterLegalMove(turn, boardMap, start, end)) {
            Log.d(TAG, String.format("Illegal move: piece at %s to %s would put king in check", start, end));
            return false;
        }

        return true;
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
            if (!isCheckAfterLegalMove(moverColour, this.boardMap, position, endPos)) {
                nonCheckPositions.add(endPos);
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
        Map<Position, BasePiece> copyBoardMap = new HashMap<>(boardMap);
        BasePiece piece = copyBoardMap.get(start);
        copyBoardMap.remove(start);
        copyBoardMap.put(end, piece);

        return isCheck(colour, copyBoardMap);
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
