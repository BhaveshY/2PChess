package model;

import common.Colour;
import common.Position;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static common.Position.*;
import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void testGetHighlightPolygons() {
        BasePiece king = new King(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();
        boardMap.put(E2R, king);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, E2R);
        assertTrue(actualKingMoves.contains(E3R));
    }

    @Test
    void testGetHighlightPolygonsWithFriendlyPiece() {
        BasePiece king = new King(Colour.BLACK);
        BasePiece piece = new Pawn(Colour.BLACK);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position startPosition = E4B;
        Position endPosition = D3B;

        boardMap.put(startPosition, king);
        boardMap.put(endPosition, piece);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, startPosition);
        assertFalse(actualKingMoves.contains(endPosition));
    }

    @Test
    void testGetHighlightPolygonsWithEnemyPiece() {
        BasePiece king = new King(Colour.BLACK);
        BasePiece piece = new Pawn(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position startPosition = E4B;
        Position endPosition = D3B;

        boardMap.put(startPosition, king);
        boardMap.put(endPosition, piece);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, startPosition);
        assertTrue(actualKingMoves.contains(endPosition));
    }

    @Test
    void testGetHighlightPolygonsAllDirections() {
        BasePiece king = new King(Colour.BLACK);
        Map<Position, BasePiece> boardMap = new HashMap<>();
        Position startPosition = E4B;
        boardMap.put(startPosition, king);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, startPosition);
        
        // Test key positions in each direction
        assertTrue(actualKingMoves.contains(E5B)); // Forward
        assertTrue(actualKingMoves.contains(E3B)); // Backward
        assertTrue(actualKingMoves.contains(D4B)); // Left
        assertTrue(actualKingMoves.contains(F4B)); // Right
        assertTrue(actualKingMoves.contains(D3B)); // Diagonal back-left
        assertTrue(actualKingMoves.contains(F3B)); // Diagonal back-right
        assertTrue(actualKingMoves.contains(D5B)); // Diagonal forward-left
        assertTrue(actualKingMoves.contains(F5B)); // Diagonal forward-right
    }

    @Test
    void testCastlingKingSide() {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPosition = E1R;
        Position rookPosition = H1R;

        boardMap.put(kingPosition, king);
        boardMap.put(rookPosition, rook);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, kingPosition);
        assertTrue(actualKingMoves.contains(G1R));
    }

    @Test
    void testCastlingKingSideBlocked() {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Position kingPosition = E1R;
        Position rookPosition = H1R;
        Position knightPosition = G1R;

        Map<Position, BasePiece> boardMap = new HashMap<>();
        boardMap.put(kingPosition, king);
        boardMap.put(rookPosition, rook);
        boardMap.put(knightPosition, new Knight(Colour.WHITE));

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, kingPosition);
        assertFalse(actualKingMoves.contains(G1R));
    }

    @Test
    void testCastlingQueenSide() {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Map<Position, BasePiece> boardMap = new HashMap<>();

        Position kingPosition = E1R;
        Position rookPosition = A1R;

        boardMap.put(kingPosition, king);
        boardMap.put(rookPosition, rook);

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, kingPosition);
        assertTrue(actualKingMoves.contains(C1R));
    }

    @Test
    void testCastlingQueenSideBlocked() {
        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        Position kingPosition = E1R;
        Position rookPosition = A1R;
        Position knightPosition = C1R;

        Map<Position, BasePiece> boardMap = new HashMap<>();
        boardMap.put(kingPosition, king);
        boardMap.put(rookPosition, rook);
        boardMap.put(knightPosition, new Knight(Colour.WHITE));

        Set<Position> actualKingMoves = king.getHighlightPolygons(boardMap, kingPosition);
        assertFalse(actualKingMoves.contains(C1R));
    }
}
