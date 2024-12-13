package model;

import com.google.common.collect.ImmutableSet;
import common.Colour;
import common.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

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
        boardMap = board.boardMap;
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece pawn = new Pawn(Colour.WHITE);
        assertNotEquals(0, pawn.directions.length);
    }

    @Test
    void isLegalMove_pawnMoveForwardToEmptySquare_True() {
        boardMap.clear();
        Position startPos = Position.valueOf("E2R");
        Position endPos = Position.valueOf("E3R");

        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn);
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualPawnMoves.contains(endPos));
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void isLegalMove_pawnAbsentFromStartPosition_False(Colour colour) {
        Position startPos = Position.valueOf("E4" + colour.toString().charAt(0));
        Position endPos = Position.valueOf("E6" + colour.toString().charAt(0));

        BasePiece pawn = new Pawn(colour);
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);
        assertFalse(actualPawnMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_pawnMoveForwardToTakeOpponentPiece_False(BasePiece piece) {
        Position startPos = Position.valueOf("E2" + piece.getColour().toString().charAt(0));
        Position endPos = Position.valueOf("E3" + piece.getColour().toString().charAt(0));

        BasePiece pawn = new Pawn(piece.getColour());
        boardMap.put(startPos, pawn);
        boardMap.put(endPos, piece);
        
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);
        assertFalse(actualPawnMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_pawnMoveDiagonalToTakeOpponentPiece_True(BasePiece piece) {
        if(piece.getColour() == Colour.WHITE) return;

        BasePiece pawn = new Pawn(Colour.WHITE);
        Position startPos = Position.valueOf("E2R");
        Position endPos = Position.valueOf("F3" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, pawn);
        boardMap.put(endPos, piece);
        
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualPawnMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_pawnTakesItsColourPiece_False(BasePiece piece) {
        BasePiece pawn = new Pawn(piece.getColour());
        Position startPos = Position.valueOf("E2" + piece.getColour().toString().charAt(0));
        Position endPos = Position.valueOf("F3" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, pawn);
        boardMap.put(endPos, piece);
        
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);
        assertFalse(actualPawnMoves.contains(endPos));
    }

    @Test
    void getHighlightPolygons_pawnInitialPosition_presentInPolygonList() {
        boardMap.clear();
        Position startPos = Position.valueOf("E2R");

        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn);

        Set<Position> expectedPawnMoves = ImmutableSet.of(
            Position.valueOf("E3R"),
            Position.valueOf("E4R")
        );
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);

        assertEquals(expectedPawnMoves, actualPawnMoves);
    }

    @Test
    void getHighlightPolygons_pawnAlreadyMoved_presentInPolygonList() {
        boardMap.clear();
        Position startPos = Position.valueOf("E4R");

        BasePiece pawn = new Pawn(Colour.WHITE);
        boardMap.put(startPos, pawn);

        Set<Position> expectedPawnMoves = ImmutableSet.of(
            Position.valueOf("E5R")
        );
        Set<Position> actualPawnMoves = pawn.getHighlightPolygons(boardMap, startPos);

        assertEquals(expectedPawnMoves, actualPawnMoves);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void toString_initPawnAllColours_correctStringFormat(Colour colour) {
        BasePiece pawn = new Pawn(colour);
        String expectedFormat = colour.toString() + "P";
        assertEquals(expectedFormat, pawn.toString());
    }
}
