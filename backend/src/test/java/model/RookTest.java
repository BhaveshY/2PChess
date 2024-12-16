package model;

import com.google.common.collect.ImmutableSet;
import helper.Colour;
import helper.Position;
import helper.InvalidPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link Rook} class.
 * Contains various tests to verify the behavior of the Rook piece in the game.
 */
class RookTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece rook = new Rook(Colour.WHITE);
        assertNotEquals(0, rook.directions.length);
    }

    @Test
    void isLegalMove_whiteRookMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 4);   // e6

        BasePiece rook = new Rook(Colour.WHITE);
        boardMap.put(startPos, rook);
        Set<Position> actualRookMoves = rook.getPossibleMoves(boardMap, startPos);
        assertTrue(actualRookMoves.contains(endPos));
    }

    @Test
    void isLegalMove_blackRookMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 2, 4);   // e6

        BasePiece rook = new Rook(Colour.BLACK);
        boardMap.put(startPos, rook);
        Set<Position> actualRookMoves = rook.getPossibleMoves(boardMap, startPos);
        assertTrue(actualRookMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteRookCapturesOwnPiece_False() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 4);   // e6

        BasePiece rook = new Rook(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, rook);
        boardMap.put(endPos, pawn);
        Set<Position> actualRookMoves = rook.getPossibleMoves(boardMap, startPos);
        assertFalse(actualRookMoves.contains(endPos));
    }


    @Test
    void check_whiteRooksInInitialPosition_True() throws InvalidPositionException {
        Position a1 = Position.get(Colour.WHITE, 7, 0); // a1
        Position h1 = Position.get(Colour.WHITE, 7, 7); // h1
        assertTrue(boardMap.get(a1) instanceof Rook);
        assertTrue(boardMap.get(h1) instanceof Rook);
        assertEquals(Colour.WHITE, boardMap.get(a1).getColour());
        assertEquals(Colour.WHITE, boardMap.get(h1).getColour());
    }

    @Test
    void check_blackRooksInInitialPosition_True() throws InvalidPositionException {
        Position a8 = Position.get(Colour.BLACK, 0, 0); // a8
        Position h8 = Position.get(Colour.BLACK, 0, 7); // h8
        assertTrue(boardMap.get(a8) instanceof Rook);
        assertTrue(boardMap.get(h8) instanceof Rook);
        assertEquals(Colour.BLACK, boardMap.get(a8).getColour());
        assertEquals(Colour.BLACK, boardMap.get(h8).getColour());
    }

    @Test
    void getPossibleMoves_whiteRookInCenter_allValidMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4

        BasePiece rook = new Rook(Colour.WHITE);
        boardMap.put(startPos, rook);

        Set<Position> expectedRookMoves = ImmutableSet.<Position>builder()
            .add(Position.get(Colour.WHITE, 7, 4)) // e1
            .add(Position.get(Colour.WHITE, 6, 4)) // e2
            .add(Position.get(Colour.WHITE, 5, 4)) // e3
            .add(Position.get(Colour.WHITE, 3, 4)) // e5
            .add(Position.get(Colour.WHITE, 2, 4)) // e6
            .add(Position.get(Colour.WHITE, 1, 4)) // e7
            .add(Position.get(Colour.WHITE, 0, 4)) // e8
            .add(Position.get(Colour.WHITE, 4, 0)) // a4
            .add(Position.get(Colour.WHITE, 4, 1)) // b4
            .add(Position.get(Colour.WHITE, 4, 2)) // c4
            .add(Position.get(Colour.WHITE, 4, 3)) // d4
            .add(Position.get(Colour.WHITE, 4, 5)) // f4
            .add(Position.get(Colour.WHITE, 4, 6)) // g4
            .add(Position.get(Colour.WHITE, 4, 7)) // h4
            .build();

        Set<Position> actualRookMoves = rook.getPossibleMoves(boardMap, startPos);
        assertEquals(expectedRookMoves, actualRookMoves);
    }

    @Test
    void toString_whiteRook_correctFormat() {
        BasePiece rook = new Rook(Colour.WHITE);
        assertEquals("WR", rook.toString());
    }

    @Test
    void toString_blackRook_correctFormat() {
        BasePiece rook = new Rook(Colour.BLACK);
        assertEquals("BR", rook.toString());
    }
}
