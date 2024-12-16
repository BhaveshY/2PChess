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
 * Test class for the {@link Pawn} class.
 * Contains various tests to verify the behavior of the Pawn piece in the game.
 */
class PawnTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece pawn = new Pawn(Colour.WHITE);
        assertNotEquals(0, pawn.directions.length);
    }

    @Test
    void isLegalMove_whitePawnMoveForwardToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 6, 4); // e2
        Position endPos = Position.get(Colour.WHITE, 5, 4);   // e3

        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn);
        Set<Position> actualPawnMoves = pawn.getPossibleMoves(boardMap, startPos);
        assertTrue(actualPawnMoves.contains(endPos));
    }

    @Test
    void isLegalMove_blackPawnMoveForwardToEmptySquare_True() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 1, 4); // e7
        Position endPos = Position.get(Colour.BLACK, 2, 4);   // e6

        BasePiece pawn = new Pawn(Colour.BLACK);
        boardMap.put(startPos, pawn);
        Set<Position> actualPawnMoves = pawn.getPossibleMoves(boardMap, startPos);
        assertTrue(actualPawnMoves.contains(endPos));
    }




    @Test
    void isLegalMove_whitePawnCaptureOwnPiece_False() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 6, 4); // e2
        Position endPos = Position.get(Colour.WHITE, 5, 5);   // f3

        BasePiece pawn1 = new Pawn(Colour.WHITE);
        BasePiece pawn2 = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn1);
        boardMap.put(endPos, pawn2);
        
        Set<Position> actualPawnMoves = pawn1.getPossibleMoves(boardMap, startPos);
        assertFalse(actualPawnMoves.contains(endPos));
    }

    @Test
    void getHighlightSquares_whitePawnInitialPosition_correctMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.WHITE, 6, 4); // e2

        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn);

        Set<Position> expectedPawnMoves = ImmutableSet.of(
            Position.get(Colour.WHITE, 5, 4), // e3
            Position.get(Colour.WHITE, 4, 4)  // e4
        );
        Set<Position> actualPawnMoves = pawn.getPossibleMoves(boardMap, startPos);

        assertEquals(expectedPawnMoves, actualPawnMoves);
    }

    @Test
    void getHighlightSquares_blackPawnInitialPosition_correctMoves() throws InvalidPositionException {
        boardMap.clear();
        Position startPos = Position.get(Colour.BLACK, 1, 4); // e7

        BasePiece pawn = new Pawn(Colour.BLACK);
        boardMap.put(startPos, pawn);

        Set<Position> expectedPawnMoves = ImmutableSet.of(
            Position.get(Colour.BLACK, 2, 4), // e6
            Position.get(Colour.BLACK, 3, 4)  // e5
        );
        Set<Position> actualPawnMoves = pawn.getPossibleMoves(boardMap, startPos);

        assertEquals(expectedPawnMoves, actualPawnMoves);
    }

    @Test
    void toString_whitePawn_correctFormat() {
        BasePiece pawn = new Pawn(Colour.WHITE);
        assertEquals("WP", pawn.toString());
    }

    @Test
    void toString_blackPawn_correctFormat() {
        BasePiece pawn = new Pawn(Colour.BLACK);
        assertEquals("BP", pawn.toString());
    }
}
