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
 * Test class for the {@link Rook} class.
 * Contains various tests to verify the behavior of the Rook piece in the game.
 */
class RookTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece rook = new Rook(Colour.WHITE);
        assertNotEquals(0, rook.directions.length);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void isLegalMove_rookMovesToEmptySquare_True(Colour colour) {
        boardMap.clear();
        Position startPos = Position.valueOf("E4" + colour.toString().charAt(0));
        Position endPos = Position.valueOf("E6" + colour.toString().charAt(0));

        BasePiece rook = new Rook(colour);
        boardMap.put(startPos, rook);
        Set<Position> actualRookMoves = rook.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualRookMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_rookTakesItsColourPiece_False(BasePiece piece) {
        BasePiece rook = new Rook(piece.getColour());
        Position startPos = Position.valueOf("E4" + piece.getColour().toString().charAt(0));
        Position endPos = Position.valueOf("E6" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, rook);
        boardMap.put(endPos, piece);
        Set<Position> actualRookMoves = rook.getHighlightPolygons(boardMap, startPos);
        assertFalse(actualRookMoves.contains(endPos));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_rookTakesDifferentColourPiece_True(BasePiece piece) {
        Colour rookColour = piece.getColour().next();
        BasePiece rook = new Rook(rookColour);
        Position startPos = Position.valueOf("E4" + rookColour.toString().charAt(0));
        Position endPos = Position.valueOf("E6" + piece.getColour().toString().charAt(0));

        boardMap.put(startPos, rook);
        boardMap.put(endPos, piece);
        Set<Position> actualRookMoves = rook.getHighlightPolygons(boardMap, startPos);
        assertTrue(actualRookMoves.contains(endPos));
    }

    @ParameterizedTest
    @EnumSource(value = Position.class, names = {"A1R", "H1R", "A1B", "H1B"})
    void check_rookPresentInInitialPosition_True(Position position) {
        BasePiece piece = boardMap.get(position);
        assertInstanceOf(Rook.class, piece);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void getHighlightPolygons_validPolygons_presentInPolygonList(Colour colour) {
        boardMap.clear();
        Position startPos = Position.valueOf("E4" + colour.toString().charAt(0));

        BasePiece rook = new Rook(colour);
        boardMap.put(startPos, rook);

        Set<Position> expectedRookMoves = ImmutableSet.of(
            Position.valueOf("E1" + colour.toString().charAt(0)),
            Position.valueOf("E2" + colour.toString().charAt(0)),
            Position.valueOf("E3" + colour.toString().charAt(0)),
            Position.valueOf("E5" + colour.toString().charAt(0)),
            Position.valueOf("E6" + colour.toString().charAt(0)),
            Position.valueOf("E7" + colour.toString().charAt(0)),
            Position.valueOf("E8" + colour.toString().charAt(0)),
            Position.valueOf("A4" + colour.toString().charAt(0)),
            Position.valueOf("B4" + colour.toString().charAt(0)),
            Position.valueOf("C4" + colour.toString().charAt(0)),
            Position.valueOf("D4" + colour.toString().charAt(0)),
            Position.valueOf("F4" + colour.toString().charAt(0)),
            Position.valueOf("G4" + colour.toString().charAt(0)),
            Position.valueOf("H4" + colour.toString().charAt(0))
        );
        Set<Position> actualRookMoves = rook.getHighlightPolygons(boardMap, startPos);

        assertEquals(expectedRookMoves, actualRookMoves);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void toString_initRookAllColours_correctStringFormat(Colour colour) {
        BasePiece rook = new Rook(colour);
        String expectedFormat = colour.toString() + "R";
        assertEquals(expectedFormat, rook.toString());
    }
}
