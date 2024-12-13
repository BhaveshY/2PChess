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
        int pawnRow = colour == Colour.WHITE ? 1 : 6;
        int mainRow = colour == Colour.WHITE ? 0 : 7;

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
        if (isLegalMove(start, end)) {
            BasePiece mover = boardMap.get(start);
            boardMap.remove(start);

            if (mover instanceof Pawn && end.getRow() == (mover.getColour() == Colour.WHITE ? 7 : 0)) {
                boardMap.put(end, new Queen(mover.getColour()));
            } else {
                boardMap.put(end, mover);
            }

            if (mover instanceof King && start.getColumn() == 4 && start.getRow() == (mover.getColour() == Colour.WHITE ? 0 : 7)) {
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

            if (isCheckMate(turn.next(), boardMap)) {
                gameOver = true;
                winner = mover.getColour().toString();
            }

            turn = turn.next();
        } else {
            throw new InvalidMoveException("Illegal Move: " + start + "-" + end);
        }
    }

    public boolean isLegalMove(Position start, Position end) {
        BasePiece mover = getPiece(start);
        if (mover == null) {
            return false;
        }
        Colour moverCol = mover.getColour();
        if (highlightPolygons.isEmpty()) {
            highlightPolygons = mover.getPossibleMoves(this.boardMap, start);
        }
        if (highlightPolygons.contains(end)) {
            if (isCheck(turn, boardMap) && isCheckAfterLegalMove(turn, boardMap, start, end)) {
                Log.d(TAG, "Colour " + moverCol + " is in check, this move doesn't help. Do again!!");
                return false;
            } else if (isCheckAfterLegalMove(turn, boardMap, start, end)) {
                Log.d(TAG, "Colour " + moverCol + " will be in check after this move");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public Colour getTurn() {
        return turn;
    }

    private BasePiece getPiece(Position position) {
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