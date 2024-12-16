package service;

import helper.Colour;
import helper.GameState;
import entity.Board;
import org.springframework.stereotype.Component;
import utility.BoardAdapter;

/**
 * Manages the state and progression of the chess game.
 * 
 * <p>This class is responsible for:
 * <ul>
 *   <li>Maintaining the game board state</li>
 *   <li>Managing turn progression</li>
 *   <li>Providing game state information</li>
 * </ul>
 * 
 * <p>The manager ensures thread-safe access to game state and
 * coordinates state transitions during gameplay.
 * 
 * Manages the game state and turn progression
 * 
 * @see Board
 * @see GameState
 * @see Colour
 * @version 1.0
 */
@Component
public class GameStateManager {
    /** The game board instance */
    private final Board board;
    
    /** The current player's turn */
    private Colour currentTurn;

    /**
     * Creates a new game state manager with initial game setup.
     * 
     * <p>Initializes:
     * <ul>
     *   <li>New game board</li>
     *   <li>Sets White as starting player</li>
     * </ul>
     */
    public GameStateManager() {
        this.board = new Board();
        this.currentTurn = Colour.WHITE;
    }

    /**
     * Gets the current state of the game.
     * 
     * <p>Returns a complete snapshot of the game state including:
     * <ul>
     *   <li>Board configuration</li>
     *   <li>Piece positions</li>
     *   <li>Game status</li>
     * </ul>
     * 
     * @return Current game state
     */
    public GameState getCurrentState() {
        return BoardAdapter.convertModelBoardToGameState(board);
    }

    /**
     * Gets the color of the current player's turn.
     * 
     * @return Color of the current player
     */
    public Colour getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Advances the game to the next player's turn.
     * 
     * <p>Turn order: WHITE -> BLACK -> WHITE
     */
    public void advanceTurn() {
        currentTurn = currentTurn.next();
    }

    /**
     * Gets the game board instance.
     * 
     * @return The current game board
     */
    public Board getBoard() {
        return board;
    }
}