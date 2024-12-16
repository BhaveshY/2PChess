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
 * This class contains unit tests for the Knight class.
 */
class KnightTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece knight = new Knight(Colour.WHITE);
        assertNotEquals(0, knight.directions.length);
    }

    @Test
    void isLegalMove_whiteKnightMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 5);   // f6

        BasePiece knight = new Knight(Colour.WHITE);
        boardMap.put(startPos, knight);
        Set<Position> actualKnightMoves = knight.getPossibleMoves(boardMap, startPos);
        assertTrue(actualKnightMoves.contains(endPos));
    }

    @Test
    void isLegalMove_blackKnightMovesToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 4, 4); // e4
        Position endPos = Position.get(Colour.BLACK, 2, 5);   // f6

        BasePiece knight = new Knight(Colour.BLACK);
        boardMap.put(startPos, knight);
        Set<Position> actualKnightMoves = knight.getPossibleMoves(boardMap, startPos);
        assertTrue(actualKnightMoves.contains(endPos));
    }

    @Test
    void isLegalMove_whiteKnightCapturesOwnPiece_False() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position endPos = Position.get(Colour.WHITE, 2, 5);   // f6

        BasePiece knight = new Knight(Colour.WHITE);
        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, knight);
        boardMap.put(endPos, pawn);
        Set<Position> actualKnightMoves = knight.getPossibleMoves(boardMap, startPos);
        assertFalse(actualKnightMoves.contains(endPos));
    }


    @Test
    void check_whiteKnightsInInitialPosition_True() throws InvalidPositionException {
        Position b1 = Position.get(Colour.WHITE, 7, 1); // b1
        Position g1 = Position.get(Colour.WHITE, 7, 6); // g1
        assertTrue(boardMap.get(b1) instanceof Knight);
        assertTrue(boardMap.get(g1) instanceof Knight);
        assertEquals(Colour.WHITE, boardMap.get(b1).getColour());
        assertEquals(Colour.WHITE, boardMap.get(g1).getColour());
    }

    @Test
    void check_blackKnightsInInitialPosition_True() throws InvalidPositionException {
        Position b8 = Position.get(Colour.BLACK, 0, 1); // b8
        Position g8 = Position.get(Colour.BLACK, 0, 6); // g8
        assertTrue(boardMap.get(b8) instanceof Knight);
        assertTrue(boardMap.get(g8) instanceof Knight);
        assertEquals(Colour.BLACK, boardMap.get(b8).getColour());
        assertEquals(Colour.BLACK, boardMap.get(g8).getColour());
    }

    @Test
    void getPossibleMoves_whiteKnightInCenter_allValidMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 4, 4); // e4

        BasePiece knight = new Knight(Colour.WHITE);
        boardMap.put(startPos, knight);

        Set<Position> expectedKnightMoves = ImmutableSet.<Position>builder()
            .add(Position.get(Colour.WHITE, 2, 5)) // f6
            .add(Position.get(Colour.WHITE, 2, 3)) // d6
            .add(Position.get(Colour.WHITE, 3, 6)) // g5
            .add(Position.get(Colour.WHITE, 3, 2)) // c5
            .add(Position.get(Colour.WHITE, 5, 6)) // g3
            .add(Position.get(Colour.WHITE, 5, 2)) // c3
            .add(Position.get(Colour.WHITE, 6, 5)) // f2
            .add(Position.get(Colour.WHITE, 6, 3)) // d2
            .build();

        Set<Position> actualKnightMoves = knight.getPossibleMoves(boardMap, startPos);
        assertEquals(expectedKnightMoves, actualKnightMoves);
    }

    @Test
    void toString_whiteKnight_correctFormat() {
        BasePiece knight = new Knight(Colour.WHITE);
        assertEquals("WN", knight.toString());
    }

    @Test
    void toString_blackKnight_correctFormat() {
        BasePiece knight = new Knight(Colour.BLACK);
        assertEquals("BN", knight.toString());
    }
}