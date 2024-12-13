package model;

import common.Colour;
import common.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static common.Position.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link Queen} class.
 * Contains various tests to verify the behavior of the Queen piece in the game.
 */
class QueenTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    /**
     * Initializes a new Board instance before each test.
     */
    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    /**
     * Tests the setupDirections method,
     * expecting the Queen movement directions to be non-empty.
     */
    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False() {
        BasePiece queen = new Queen(Colour.BLACK);
        assertNotEquals(0, queen.directions.length);
    }

    /**
     * Parameterized test for checking if the queen is present in its initial position,
     * expecting true.
     *
     * @param position Initial position of the queen
     */
    @ParameterizedTest
    @EnumSource(value = Position.class, names = {"D1B", "D1R"})
    void check_queenPresentInInitialPosition_True(Position position) {
        BasePiece piece = boardMap.get(position);
        assertInstanceOf(Queen.class, piece);
    }

    /**
     * Parameterized test for isLegalMove method when the queen moves to an empty square, expecting true.
     *
     * @param colour Colour of the queen
     */
    @ParameterizedTest
    @EnumSource(Colour.class)
    void isLegalMove_queenMovesToEmptySquare_True(Colour colour) {
        Map<Position, BasePiece> testBoardMap = new HashMap<>();
        Position queenPosition = colour == Colour.BLACK ? E4B : E4R;
        Position targetPosition = colour == Colour.BLACK ? F3B : F3R;
        
        BasePiece queen = new Queen(colour);
        testBoardMap.put(queenPosition, queen);
        Set<Position> actualQueenMoves = queen.getHighlightPolygons(testBoardMap, queenPosition);
        assertTrue(actualQueenMoves.contains(targetPosition));
    }

    /**
     * Parameterized test for isLegalMove method
     * when the queen takes a piece of its own colour,
     * expecting false.
     *
     * @param piece Piece to be placed on the board
     */
    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_queenTakesItsColourPiece_False(BasePiece piece) {
        Map<Position, BasePiece> testBoardMap = new HashMap<>();
        Position startPosition = piece.getColour() == Colour.BLACK ? E4B : E4R;
        Position endPosition = piece.getColour() == Colour.BLACK ? F3B : F3R;
        
        BasePiece queen = new Queen(piece.getColour());
        testBoardMap.put(startPosition, queen);
        testBoardMap.put(endPosition, piece);
        Set<Position> actualQueenMoves = queen.getHighlightPolygons(testBoardMap, startPosition);
        assertFalse(actualQueenMoves.contains(endPosition));
    }

    /**
     * Parameterized test for isLegalMove method
     * when the queen takes a piece of a different colour,
     * expecting true.
     *
     * @param piece Piece to be placed on the board
     */
    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_queenTakesDifferentColourPiece_True(BasePiece piece) {
        Map<Position, BasePiece> testBoardMap = new HashMap<>();
        Colour queenColour = piece.getColour().next();
        Position startPosition = queenColour == Colour.BLACK ? E4B : E4R;
        Position endPosition = piece.getColour() == Colour.BLACK ? F3B : F3R;
        
        BasePiece queen = new Queen(queenColour);
        testBoardMap.put(startPosition, queen);
        testBoardMap.put(endPosition, piece);
        Set<Position> actualQueenMoves = queen.getHighlightPolygons(testBoardMap, startPosition);
        assertTrue(actualQueenMoves.contains(endPosition));
    }

    /**
     * Parameterized test for getHighlightPolygons method,
     * expecting valid polygons to be present in the list.
     *
     * @param colour Colour of the queen
     */
    @ParameterizedTest
    @EnumSource(Colour.class)
    void getHighlightPolygons_validPolygons_presentInPolygonList(Colour colour) {
        Map<Position, BasePiece> testBoardMap = new HashMap<>();
        Position startPosition = colour == Colour.BLACK ? E4B : E4R;
        
        BasePiece queen = new Queen(colour);
        testBoardMap.put(startPosition, queen);
        
        Set<Position> actualQueenMoves = queen.getHighlightPolygons(testBoardMap, startPosition);
        
        // Test some key positions that should be reachable
        assertTrue(actualQueenMoves.contains(colour == Colour.BLACK ? F3B : F3R)); // Diagonal move
        assertTrue(actualQueenMoves.contains(colour == Colour.BLACK ? E3B : E3R)); // Vertical move
        assertTrue(actualQueenMoves.contains(colour == Colour.BLACK ? F4B : F4R)); // Horizontal move
    }

    /**
     * Test for toString method,
     * expecting correct string format for queen initialization.
     */
    @Test
    void toString_initQueen_correctStringFormat() {
        BasePiece whiteQueen = new Queen(Colour.WHITE);
        BasePiece blackQueen = new Queen(Colour.BLACK);
        
        assertEquals("WQ", whiteQueen.toString());
        assertEquals("BQ", blackQueen.toString());
    }
}
