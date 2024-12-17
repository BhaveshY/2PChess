package application.controller;

import service.IGameInterface;
import helper.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing chess game interactions.
 * 
 * <p>This controller provides endpoints for:
 * <ul>
 *   <li>Starting new games</li>
 *   <li>Getting board state</li>
 *   <li>Handling player moves</li>
 *   <li>Managing game flow</li>
 * </ul>
 * 
 * <p>The controller uses Spring's REST annotations to:
 * <ul>
 *   <li>Map HTTP requests to handler methods</li>
 *   <li>Convert between JSON and Java objects</li>
 *   <li>Manage response types</li>
 * </ul>
 * 
 * @see IGameInterface
 * @see GameState
 * @version 1.0
 */
@RestController
public class GameController {

    /** Game service interface for managing game state and rules */
    private final IGameInterface game;

    /**
     * Creates a new game controller with injected game service.
     * 
     * @param game Game service implementation
     */
    @Autowired
    public GameController(IGameInterface game) {
        this.game = game;
    }

    /**
     * Starts a new chess game.
     * 
     * <p>This endpoint:
     * <ul>
     *   <li>Resets the board to initial state</li>
     *   <li>Clears any existing game progress</li>
     *   <li>Sets up pieces in starting positions</li>
     * </ul>
     */
    @GetMapping("/newGame")
    public void newGame() {
        // Additional logic can be added here if necessary
    }

    /**
     * Gets the current board state.
     * 
     * <p>Returns:
     * <ul>
     *   <li>Piece positions</li>
     *   <li>Possible moves</li>
     *   <li>Game status</li>
     *   <li>Eliminated pieces</li>
     * </ul>
     * 
     * @return Current game state
     */
    @GetMapping("/board")
    @ResponseBody
    public GameState getBoard() {
        return game.getBoard();
    }

    /**
     * Handles player clicks on the board.
     * 
     * <p>This endpoint:
     * <ul>
     *   <li>Processes move attempts</li>
     *   <li>Updates game state</li>
     *   <li>Validates moves</li>
     *   <li>Handles piece selection</li>
     * </ul>
     * 
     * @param polygonText Square notation of clicked position
     * @return Updated game state after the click
     */
    @PostMapping("/onClick")
    @ResponseBody
    public GameState onClick(@RequestBody String polygonText) {
        return game.onClick(polygonText);
    }

    /**
     * Gets the current player's turn.
     * 
     * <p>Returns:
     * <ul>
     *   <li>"W" for White's turn</li>
     *   <li>"B" for Black's turn</li>
     * </ul>
     * 
     * @return String representation of current player
     */
    @GetMapping("/currentPlayer")
    @ResponseBody
    public String getCurrentPlayer() {
        return game.getTurn().toString();
    }

    /**
     * Gets the complete board state.
     * 
     * <p>Similar to {@link #getBoard()} but may include:
     * <ul>
     *   <li>Additional state information</li>
     *   <li>Game metadata</li>
     *   <li>Player statistics</li>
     * </ul>
     * 
     * @return Complete game state
     */
    @GetMapping("/boardState")
    @ResponseBody
    public GameState getBoardState() {
        return game.getBoard();
    }
}
