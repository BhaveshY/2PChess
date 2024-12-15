package model;

import com.google.common.collect.ImmutableSet;
import common.Colour;
import common.Position;
import common.InvalidPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece bishop = new Bishop(Colour.WHITE);
        assertNotEquals(0, bishop.directions.length);
    }

    @Test
    void isLegalMove_whiteBishopMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 6);   // g6

        BasePiece bishop = new Bishop(Colour.WHITE);
        boardMap.put(startPos, bishop);
        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPos);
        assertTrue(actualBishopMoves.contains(endPos));
    }

    @Test
    void isLegalMove_blackBishopMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 2, 6);   // g6

        BasePiece bishop = new Bishop(Colour.BLACK);
        boardMap.put(startPos, bishop);
        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPos);
        assertTrue(actualBishopMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteBishopCapturesOwnPiece_False() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 3, 5);   // f5

        BasePiece bishop = new Bishop(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, bishop);
        boardMap.put(endPos, pawn);
        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPos);
        assertFalse(actualBishopMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteBishopCapturesBlackPiece_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 3, 5);   // f5

        BasePiece bishop = new Bishop(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.BLACK);
        boardMap.put(startPos, bishop);
        boardMap.put(endPos, pawn);
        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPos);
        assertTrue(actualBishopMoves.contains(endPos));
    }

    @Test
    void check_whiteBishopsInInitialPosition_True() throws InvalidPositionException {
        Position c1 = Position.get(Colour.WHITE, 7, 2); // c1
        Position f1 = Position.get(Colour.WHITE, 7, 5); // f1
        assertTrue(boardMap.get(c1) instanceof Bishop);
        assertTrue(boardMap.get(f1) instanceof Bishop);
        assertEquals(Colour.WHITE, boardMap.get(c1).getColour());
        assertEquals(Colour.WHITE, boardMap.get(f1).getColour());
    }

    @Test
    void check_blackBishopsInInitialPosition_True() throws InvalidPositionException {
        Position c8 = Position.get(Colour.BLACK, 0, 2); // c8
        Position f8 = Position.get(Colour.BLACK, 0, 5); // f8
        assertTrue(boardMap.get(c8) instanceof Bishop);
        assertTrue(boardMap.get(f8) instanceof Bishop);
        assertEquals(Colour.BLACK, boardMap.get(c8).getColour());
        assertEquals(Colour.BLACK, boardMap.get(f8).getColour());
    }

    @Test
    void getPossibleMoves_whiteBishopInCenter_allValidMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4

        BasePiece bishop = new Bishop(Colour.WHITE);
        boardMap.put(startPos, bishop);

        Set<Position> expectedBishopMoves = ImmutableSet.<Position>builder()
            // Up-right diagonal
            .add(Position.get(Colour.WHITE, 3, 5)) // f5
            .add(Position.get(Colour.WHITE, 2, 6)) // g6
            .add(Position.get(Colour.WHITE, 1, 7)) // h7
            // Up-left diagonal
            .add(Position.get(Colour.WHITE, 3, 3)) // d5
            .add(Position.get(Colour.WHITE, 2, 2)) // c6
            .add(Position.get(Colour.WHITE, 1, 1)) // b7
            .add(Position.get(Colour.WHITE, 0, 0)) // a8
            // Down-right diagonal
            .add(Position.get(Colour.WHITE, 5, 5)) // f3
            .add(Position.get(Colour.WHITE, 6, 6)) // g2
            .add(Position.get(Colour.WHITE, 7, 7)) // h1
            // Down-left diagonal
            .add(Position.get(Colour.WHITE, 5, 3)) // d3
            .add(Position.get(Colour.WHITE, 6, 2)) // c2
            .add(Position.get(Colour.WHITE, 7, 1)) // b1
            .build();

        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPos);
        assertEquals(expectedBishopMoves, actualBishopMoves);
    }

    @Test
    void toString_whiteBishop_correctFormat() {
        BasePiece bishop = new Bishop(Colour.WHITE);
        assertEquals("WB", bishop.toString());
    }

    @Test
    void toString_blackBishop_correctFormat() {
        BasePiece bishop = new Bishop(Colour.BLACK);
        assertEquals("BB", bishop.toString());
    }
}
