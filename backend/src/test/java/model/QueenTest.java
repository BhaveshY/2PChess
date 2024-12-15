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

class QueenTest {
    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece queen = new Queen(Colour.WHITE);
        assertNotEquals(0, queen.directions.length);
    }

    @Test
    void isLegalMove_whiteQueenMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 6);   // g6

        BasePiece queen = new Queen(Colour.WHITE);
        boardMap.put(startPos, queen);
        Set<Position> actualQueenMoves = queen.getPossibleMoves(boardMap, startPos);
        assertTrue(actualQueenMoves.contains(endPos));
    }

    @Test
    void isLegalMove_blackQueenMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 2, 6);   // g6

        BasePiece queen = new Queen(Colour.BLACK);
        boardMap.put(startPos, queen);
        Set<Position> actualQueenMoves = queen.getPossibleMoves(boardMap, startPos);
        assertTrue(actualQueenMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteQueenCapturesOwnPiece_False() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 3, 5);   // f5

        BasePiece queen = new Queen(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, queen);
        boardMap.put(endPos, pawn);
        Set<Position> actualQueenMoves = queen.getPossibleMoves(boardMap, startPos);
        assertFalse(actualQueenMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteQueenCapturesBlackPiece_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 3, 5);   // f5

        BasePiece queen = new Queen(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.BLACK);
        boardMap.put(startPos, queen);
        boardMap.put(endPos, pawn);
        Set<Position> actualQueenMoves = queen.getPossibleMoves(boardMap, startPos);
        assertTrue(actualQueenMoves.contains(endPos));
    }

    @Test
    void check_whiteQueenInInitialPosition_True() throws InvalidPositionException {
        Position d1 = Position.get(Colour.WHITE, 7, 3); // d1
        assertTrue(boardMap.get(d1) instanceof Queen);
        assertEquals(Colour.WHITE, boardMap.get(d1).getColour());
    }

    @Test
    void check_blackQueenInInitialPosition_True() throws InvalidPositionException {
        Position d8 = Position.get(Colour.BLACK, 0, 3); // d8
        assertTrue(boardMap.get(d8) instanceof Queen);
        assertEquals(Colour.BLACK, boardMap.get(d8).getColour());
    }

    @Test
    void getPossibleMoves_whiteQueenInCenter_allValidMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4

        BasePiece queen = new Queen(Colour.WHITE);
        boardMap.put(startPos, queen);

        Set<Position> expectedQueenMoves = ImmutableSet.<Position>builder()
            // Diagonal moves (like Bishop)
            .add(Position.get(Colour.WHITE, 3, 5)) // f5
            .add(Position.get(Colour.WHITE, 2, 6)) // g6
            .add(Position.get(Colour.WHITE, 1, 7)) // h7
            .add(Position.get(Colour.WHITE, 3, 3)) // d5
            .add(Position.get(Colour.WHITE, 2, 2)) // c6
            .add(Position.get(Colour.WHITE, 1, 1)) // b7
            .add(Position.get(Colour.WHITE, 0, 0)) // a8
            .add(Position.get(Colour.WHITE, 5, 5)) // f3
            .add(Position.get(Colour.WHITE, 6, 6)) // g2
            .add(Position.get(Colour.WHITE, 7, 7)) // h1
            .add(Position.get(Colour.WHITE, 5, 3)) // d3
            .add(Position.get(Colour.WHITE, 6, 2)) // c2
            .add(Position.get(Colour.WHITE, 7, 1)) // b1
            // Straight moves (like Rook)
            .add(Position.get(Colour.WHITE, 4, 0)) // a4
            .add(Position.get(Colour.WHITE, 4, 1)) // b4
            .add(Position.get(Colour.WHITE, 4, 2)) // c4
            .add(Position.get(Colour.WHITE, 4, 3)) // d4
            .add(Position.get(Colour.WHITE, 4, 5)) // f4
            .add(Position.get(Colour.WHITE, 4, 6)) // g4
            .add(Position.get(Colour.WHITE, 4, 7)) // h4
            .add(Position.get(Colour.WHITE, 0, 4)) // e8
            .add(Position.get(Colour.WHITE, 1, 4)) // e7
            .add(Position.get(Colour.WHITE, 2, 4)) // e6
            .add(Position.get(Colour.WHITE, 3, 4)) // e5
            .add(Position.get(Colour.WHITE, 5, 4)) // e3
            .add(Position.get(Colour.WHITE, 6, 4)) // e2
            .add(Position.get(Colour.WHITE, 7, 4)) // e1
            .build();

        Set<Position> actualQueenMoves = queen.getPossibleMoves(boardMap, startPos);
        assertEquals(expectedQueenMoves, actualQueenMoves);
    }

    @Test
    void toString_whiteQueen_correctFormat() {
        BasePiece queen = new Queen(Colour.WHITE);
        assertEquals("WQ", queen.toString());
    }

    @Test
    void toString_blackQueen_correctFormat() {
        BasePiece queen = new Queen(Colour.BLACK);
        assertEquals("BQ", queen.toString());
    }
}
