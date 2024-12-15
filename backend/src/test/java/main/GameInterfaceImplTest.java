package main;

import common.Colour;
import common.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the GameInterfaceImpl class.
 */
class GameInterfaceImplTest {

    private GameInterfaceImpl gameInterfaceImpl;

    /**
     * Initializes a new GameInterfaceImpl instance before each test.
     */
    @BeforeEach
    void initBeforeEachBoardTest() {
        gameInterfaceImpl = new GameInterfaceImpl();
    }

    /**
     * Tests the onClick method when selecting an empty square, expecting no highlighted squares.
     */
    @Test
    void onClick_selectEmptySquare_noHighlight() {
        GameState response = gameInterfaceImpl.onClick("c3");
        assertEquals(0, response.getHighlightSquares().size());
    }

    /**
     * Tests the onClick method when selecting a WHITE pawn on first turn, expecting highlight.
     */
    @Test
    void onClick_selectWhitePawnFirstTurn_highlightListNonEmpty() {
        GameState response = gameInterfaceImpl.onClick("a2");
        assertFalse(response.getHighlightSquares().isEmpty());
    }

    /**
     * Tests the onClick method when selecting a non-turn BLACK pawn, expecting no highlight.
     */
    @Test
    void onClick_selectNonTurnBlackPawn_noHighlight() {
        GameState response = gameInterfaceImpl.onClick("a7");
        assertEquals(0, response.getHighlightSquares().size());
    }

    /**
     * Tests the onClick method when moving a WHITE pawn, expecting no highlight after move.
     */
    @Test
    void onClick_moveWhitePawn_noHighlight() {
        gameInterfaceImpl.onClick("b2");
        GameState response = gameInterfaceImpl.onClick("b4");
        assertEquals(0, response.getHighlightSquares().size());
    }

    /**
     * Parameterized test for onClick method with invalid square labels,
     * expecting no highlight and no board change.
     *
     * @param squareLabel Invalid square label to test
     */
    @ParameterizedTest
    @ValueSource(strings = {"k2", "", "123", "i3", "bb"})
    void onClick_invalidSquareLabel_noHighlightNoBoardChange(String squareLabel) {
        Map<String, String> oldBoard = gameInterfaceImpl.getBoard().getBoard();
        GameState response = gameInterfaceImpl.onClick(squareLabel);
        assertEquals(0, response.getHighlightSquares().size());
        assertEquals(oldBoard, response.getBoard());
    }

    /**
     * Tests the onClick method with an invalid move, expecting no highlight and no board change.
     */
    @Test
    void onClick_invalidMove_noHighlightNoBoardChange() {
        Map<String, String> oldBoard = gameInterfaceImpl.getBoard().getBoard();
        gameInterfaceImpl.onClick("a2");
        GameState response = gameInterfaceImpl.onClick("a5");
        assertEquals(0, response.getHighlightSquares().size());
        assertEquals(oldBoard, response.getBoard());
    }

    /**
     * Tests the getTurn method on game start, expecting WHITE turn.
     */
    @Test
    void getTurn_getTurnOnGameStart_whiteTurn() {
        assertEquals(Colour.WHITE, gameInterfaceImpl.getTurn());
    }

    /**
     * Tests the getTurn method after one valid move, expecting BLACK turn.
     */
    @Test
    void getTurn_getTurnAfterOneValidMove_blackTurn() {
        gameInterfaceImpl.onClick("e2"); // Select WHITE pawn
        gameInterfaceImpl.onClick("e4"); // Move WHITE pawn
        assertEquals(Colour.BLACK, gameInterfaceImpl.getTurn());
    }

    /**
     * Tests the getTurn method after two valid moves, expecting WHITE turn.
     */
    @Test
    void getTurn_getTurnAfterTwoValidMoves_whiteTurn() {
        // WHITE's turn
        gameInterfaceImpl.onClick("e2");
        gameInterfaceImpl.onClick("e4");
        // BLACK's turn
        gameInterfaceImpl.onClick("e7");
        gameInterfaceImpl.onClick("e5");
        assertEquals(Colour.WHITE, gameInterfaceImpl.getTurn());
    }

    /**
     * Tests a complete pawn capture sequence
     */
    @Test
    void onClick_pawnCapture_successfulCapture() {
        // WHITE's turn - move pawn to e4
        gameInterfaceImpl.onClick("e2");
        gameInterfaceImpl.onClick("e4");
        // BLACK's turn - move pawn to d5
        gameInterfaceImpl.onClick("d7");
        gameInterfaceImpl.onClick("d5");
        // WHITE's turn - capture BLACK pawn
        gameInterfaceImpl.onClick("e4");
        GameState response = gameInterfaceImpl.onClick("d5");
        
        Map<String, String> board = response.getBoard();
        assertEquals("WP", board.get("d5")); // WHITE pawn should be on d5
        assertNull(board.get("e4")); // Original square should be empty
    }
}
