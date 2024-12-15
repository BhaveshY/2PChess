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
 * This class contains unit tests for the GameMain class.
 */
class GameMainTest {

    private GameMain gameMain;

    /**
     * Initializes a new GameMain instance before each test.
     */
    @BeforeEach
    void initBeforeEachBoardTest() {
        gameMain = new GameMain();
    }

    /**
     * Tests the onClick method when selecting an empty square, expecting no highlighted squares.
     */
    @Test
    void onClick_selectEmptySquare_noHighlight() {
        GameState response = gameMain.onClick("c3");
        assertEquals(0, response.getHighlightSquares().size());
    }

    /**
     * Tests the onClick method when selecting a WHITE pawn on first turn, expecting highlight.
     */
    @Test
    void onClick_selectWhitePawnFirstTurn_highlightListNonEmpty() {
        GameState response = gameMain.onClick("a2");
        assertFalse(response.getHighlightSquares().isEmpty());
    }

    /**
     * Tests the onClick method when selecting a non-turn BLACK pawn, expecting no highlight.
     */
    @Test
    void onClick_selectNonTurnBluePawn_noHighlight() {
        GameState response = gameMain.onClick("a7");
        assertEquals(0, response.getHighlightSquares().size());
    }

    /**
     * Tests the onClick method when moving a WHITE pawn, expecting no highlight after move.
     */
    @Test
    void onClick_moveWhitePawn_noHighlight() {
        gameMain.onClick("b2");
        GameState response = gameMain.onClick("b4");
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
        Map<String, String> oldBoard = gameMain.getBoard().getBoard();
        GameState response = gameMain.onClick(squareLabel);
        assertEquals(0, response.getHighlightSquares().size());
        assertEquals(oldBoard, response.getBoard());
    }

    /**
     * Tests the onClick method with an invalid move, expecting no highlight and no board change.
     */
    @Test
    void onClick_invalidMove_noHighlightNoBoardChange() {
        Map<String, String> oldBoard = gameMain.getBoard().getBoard();
        gameMain.onClick("a2");
        GameState response = gameMain.onClick("a5");
        assertEquals(0, response.getHighlightSquares().size());
        assertEquals(oldBoard, response.getBoard());
    }

    /**
     * Tests the getTurn method on game start, expecting WHITE turn.
     */
    @Test
    void getTurn_getTurnOnGameStart_whiteTurn() {
        assertEquals(Colour.WHITE, gameMain.getTurn());
    }

    /**
     * Tests the getTurn method after one valid move, expecting BLACK turn.
     */
    @Test
    void getTurn_getTurnAfterOneValidMove_blackTurn() {
        gameMain.onClick("e2"); // Select WHITE pawn
        gameMain.onClick("e4"); // Move WHITE pawn
        assertEquals(Colour.BLACK, gameMain.getTurn());
    }

    /**
     * Tests the getTurn method after two valid moves, expecting WHITE turn.
     */
    @Test
    void getTurn_getTurnAfterTwoValidMoves_whiteTurn() {
        // WHITE's turn
        gameMain.onClick("e2");
        gameMain.onClick("e4");
        // BLACK's turn
        gameMain.onClick("e7");
        gameMain.onClick("e5");
        assertEquals(Colour.WHITE, gameMain.getTurn());
    }

    /**
     * Tests a complete pawn capture sequence
     */
    @Test
        void onClick_pawnCapture_successfulCapture() {
        // WHITE's turn - move pawn to e4
        gameMain.onClick("e2");
        gameMain.onClick("e4");
        // BLACK's turn - move pawn to d5
        gameMain.onClick("d7");
        gameMain.onClick("d5");
        // WHITE's turn - capture BLACK pawn
        gameMain.onClick("e4");
        GameState response = gameMain.onClick("d5");
        
        Map<String, String> board = response.getBoard();
        assertEquals("RR", board.get("d5")); // WHITE pawn should be on d5
        assertNull(board.get("e4")); // Original square should be empty
    }
}
