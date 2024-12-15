package model;

import common.Colour;
import common.Position;
import common.InvalidPositionException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void testGetPossibleMoves() throws InvalidPositionException {
        BasePiece king = new King(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();
        Position startPos = Position.get(Colour.WHITE, 6, 4); // e2
        Position endPos = Position.get(Colour.WHITE, 5, 4);   // e3
        boardMap.put(startPos, king);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, startPos);
        assertTrue(actualKingMoves.contains(endPos));
    }

    @Test
    void testGetPossibleMovesWithFriendlyPiece() throws InvalidPositionException {
        BasePiece king = new King(Colour.BLACK);
        BasePiece piece = new Pawn(Colour.BLACK);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 5, 3);   // d3
        boardMap.put(startPos, king);
        boardMap.put(endPos, piece);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, startPos);
        assertFalse(actualKingMoves.contains(endPos));
    }

    @Test
    void testGetPossibleMovesWithEnemyPiece() throws InvalidPositionException {
        BasePiece king = new King(Colour.BLACK);
        BasePiece piece = new Pawn(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 5, 3);   // d3
        boardMap.put(startPos, king);
        boardMap.put(endPos, piece);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, startPos);
        assertTrue(actualKingMoves.contains(endPos));
    }

    @Test
    void testGetPossibleMovesAllDirections() throws InvalidPositionException {
        BasePiece king = new King(Colour.BLACK);
        Map<Position, BasePiece> boardMap = new HashMap<>();
        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        boardMap.put(startPos, king);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, startPos);
        
        // Test all eight directions
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 3, 4))); // Forward
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 5, 4))); // Backward
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 4, 3))); // Left
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 4, 5))); // Right
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 5, 3))); // Diagonal back-left
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 5, 5))); // Diagonal back-right
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 3, 3))); // Diagonal forward-left
        assertTrue(actualKingMoves.contains(Position.get(Colour.BLACK, 3, 5))); // Diagonal forward-right
    }

    @Test
    void testCastlingKingSide() throws InvalidPositionException {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPos = Position.get(Colour.WHITE, 7, 4); // e1
        Position rookPos = Position.get(Colour.WHITE, 7, 7); // h1
        Position castlePos = Position.get(Colour.WHITE, 7, 6); // g1

        boardMap.put(kingPos, king);
        boardMap.put(rookPos, rook);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, kingPos);
        assertTrue(actualKingMoves.contains(castlePos));
    }

    @Test
    void testCastlingKingSideBlocked() throws InvalidPositionException {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPos = Position.get(Colour.WHITE, 7, 4); // e1
        Position rookPos = Position.get(Colour.WHITE, 7, 7); // h1
        Position blockPos = Position.get(Colour.WHITE, 7, 6); // g1
        Position castlePos = Position.get(Colour.WHITE, 7, 6); // g1

        boardMap.put(kingPos, king);
        boardMap.put(rookPos, rook);
        boardMap.put(blockPos, new Knight(Colour.WHITE));

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, kingPos);
        assertFalse(actualKingMoves.contains(castlePos));
    }

    @Test
    void testCastlingQueenSide() throws InvalidPositionException {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPos = Position.get(Colour.WHITE, 7, 4); // e1
        Position rookPos = Position.get(Colour.WHITE, 7, 0); // a1
        Position castlePos = Position.get(Colour.WHITE, 7, 2); // c1

        boardMap.put(kingPos, king);
        boardMap.put(rookPos, rook);

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, kingPos);
        assertTrue(actualKingMoves.contains(castlePos));
    }

    @Test
    void testCastlingQueenSideBlocked() throws InvalidPositionException {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPos = Position.get(Colour.WHITE, 7, 4); // e1
        Position rookPos = Position.get(Colour.WHITE, 7, 0); // a1
        Position blockPos = Position.get(Colour.WHITE, 7, 2); // c1
        Position castlePos = Position.get(Colour.WHITE, 7, 2); // c1

        boardMap.put(kingPos, king);
        boardMap.put(rookPos, rook);
        boardMap.put(blockPos, new Knight(Colour.WHITE));

        Set<Position> actualKingMoves = king.getPossibleMoves(boardMap, kingPos);
        assertFalse(actualKingMoves.contains(castlePos));
    }

    @Test
    void toString_whiteKing_correctFormat() {
        BasePiece king = new King(Colour.WHITE);
        assertEquals("WK", king.toString());
    }

    @Test
    void toString_blackKing_correctFormat() {
        BasePiece king = new King(Colour.BLACK);
        assertEquals("BK", king.toString());
    }
}
