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
 * This class contains unit tests for the Knight class.
 */
class KnightTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece knight = new Knight(Colour.WHITE);
        assertNotEquals(0, knight.directions.length);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void isLegalMove_knightMovesToEmptySquare_True(Colour colour) {
        boardMap.clear();
        Position startPos = Position.valueOf("E4" + colour.toString().charAt(0));
        Position endPos = Position.valueOf("F6" + colour.toString().charAt(0));

        BasePiece knight = new Knight(colour);
        boardMap.put(startPos, knight);
        Set<Position> actualKnightMoves = knight.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualKnightMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_knightTakesItsColourPiece_False(BasePiece piece) {
        BasePiece knight = new Knight(piece.getColour());
        Position startPos = Position.valueOf("E4" + piece.getColour().toString().charAt(0));
        Position endPos = Position.valueOf("F6" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, knight);
        boardMap.put(endPos, piece);
        Set<Position> actualKnightMoves = knight.getHighlightPolygons(boardMap, startPos);
        assertFalse(actualKnightMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_knightTakesDifferentColourPiece_True(BasePiece piece) {
        Colour knightColour = piece.getColour().next();
        BasePiece knight = new Knight(knightColour);
        Position startPos = Position.valueOf("E4" + knightColour.toString().charAt(0));
        Position endPos = Position.valueOf("F6" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, knight);
        boardMap.put(endPos, piece);
        Set<Position> actualKnightMoves = knight.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualKnightMoves.contains(endPos));
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void getHighlightPolygons_validPolygons_presentInPolygonList(Colour colour) {
        boardMap.clear();
        Position startPos = Position.valueOf("E4" + colour.toString().charAt(0));

        BasePiece knight = new Knight(colour);
        boardMap.put(startPos, knight);

        Set<Position> expectedKnightMoves = ImmutableSet.of(
            Position.valueOf("F6" + colour.toString().charAt(0)),
            Position.valueOf("D6" + colour.toString().charAt(0)),
            Position.valueOf("G5" + colour.toString().charAt(0)),
            Position.valueOf("C5" + colour.toString().charAt(0)),
            Position.valueOf("G3" + colour.toString().charAt(0)),
            Position.valueOf("C3" + colour.toString().charAt(0)),
            Position.valueOf("F2" + colour.toString().charAt(0)),
            Position.valueOf("D2" + colour.toString().charAt(0))
        );
        Set<Position> actualKnightMoves = knight.getHighlightPolygons(boardMap, startPos);

        assertEquals(expectedKnightMoves, actualKnightMoves);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void toString_initKnightAllColours_correctStringFormat(Colour colour) {
        BasePiece knight = new Knight(colour);
        String expectedFormat = colour.toString() + "N";
        assertEquals(expectedFormat, knight.toString());
    }
}