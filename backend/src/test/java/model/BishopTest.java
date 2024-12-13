package model;

import common.Colour;
import common.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Map;
import java.util.Set;

import static common.Position.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the Bishop class.
 */
class BishopTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece bishop = new Bishop(Colour.WHITE);
        assertNotEquals(0, bishop.directions.length);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/legalBishopMoves.csv")
    void isLegalMove_validMoves_True(String start, String end) {
        Position startPosition = Position.valueOf(start);
        Position endPosition = Position.valueOf(end);

        BasePiece bishop = new Bishop(startPosition.getColour());
        boardMap.put(startPosition, bishop);

        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPosition);
        assertTrue(actualBishopMoves.contains(endPosition));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/illegalBishopMoves.csv")
    void isLegalMove_invalidMoves_False(String start, String end) {
        Position startPosition = Position.valueOf(start);
        Position endPosition = Position.valueOf(end);

        BasePiece bishop = new Bishop(startPosition.getColour());
        boardMap.put(startPosition, bishop);

        Set<Position> actualBishopMoves = bishop.getPossibleMoves(boardMap, startPosition);
        assertFalse(actualBishopMoves.contains(endPosition));
    }

    @ParameterizedTest
    @EnumSource(value = Position.class, names = {"C1R", "F1R", "C8B", "F8B"})
    void check_bishopPresentInInitialPosition_True(Position position) {
        BasePiece piece = boardMap.get(position);
        assertInstanceOf(Bishop.class, piece);
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_bishopTakesItsColourPiece_False(BasePiece piece) {
        BasePiece bishop = new Bishop(piece.getColour());
        Position startPosition = piece.getColour() == Colour.WHITE ? E4R : E4B;
        Position endPosition = piece.getColour() == Colour.WHITE ? D3R : D3B;

        boardMap.put(startPosition, bishop);
        boardMap.put(endPosition, piece);

        Set<Position> actualBishopMoves = bishop.getHighlightPolygons(boardMap, startPosition);
        assertFalse(actualBishopMoves.contains(endPosition));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_bishopTakesDifferentColourPiece_True(BasePiece piece) {
        Colour bishopColor = piece.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
        BasePiece bishop = new Bishop(bishopColor);
        Position startPosition = bishopColor == Colour.WHITE ? E4R : E4B;
        Position endPosition = piece.getColour() == Colour.WHITE ? D3R : D3B;

        boardMap.put(startPosition, bishop);
        boardMap.put(endPosition, piece);
        Set<Position> actualBishopMoves = bishop.getHighlightPolygons(boardMap, startPosition);
        assertTrue(actualBishopMoves.contains(endPosition));
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void getHighlightPolygons_validPolygons_presentInPolygonList(Colour colour) {
        boardMap.clear();
        Position startPosition = colour == Colour.WHITE ? E4R : E4B;
        BasePiece bishop = new Bishop(colour);
        boardMap.put(startPosition, bishop);

        Set<Position> actualBishopMoves = bishop.getHighlightPolygons(boardMap, startPosition);
        
        // Test diagonal moves in all directions
        assertTrue(actualBishopMoves.contains(colour == Colour.WHITE ? F5R : F5B)); // Up-right
        assertTrue(actualBishopMoves.contains(colour == Colour.WHITE ? D5R : D5B)); // Up-left
        assertTrue(actualBishopMoves.contains(colour == Colour.WHITE ? F3R : F3B)); // Down-right
        assertTrue(actualBishopMoves.contains(colour == Colour.WHITE ? D3R : D3B)); // Down-left
    }

    @Test
    void toString_initBishop_correctStringFormat() {
        BasePiece whiteBishop = new Bishop(Colour.WHITE);
        BasePiece blackBishop = new Bishop(Colour.BLACK);
        
        assertEquals("WB", whiteBishop.toString());
        assertEquals("BB", blackBishop.toString());
    }
}
