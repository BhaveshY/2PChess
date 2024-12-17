package service;
import helper.Colour;
import helper.GameState;

/**
 * Main interface for the 3-Player Chess game.
 * This interface defines the core game operations and state management.
 * 
 * <p>The interface provides methods to:
 * <ul>
 *   <li>Retrieve the current game state</li>
 *   <li>Handle player interactions</li>
 *   <li>Manage turn progression</li>
 * </ul>
 * 
 * <p>Implementation classes should ensure thread safety when
 * multiple players are interacting with the game simultaneously.
 * 
 * @see GameState
 * @see Colour
 * @version 1.0
 */
public interface IGameInterface {

    /**
     * Retrieves the current game state including board layout and game information.
     * 
     * <p>This method provides a complete snapshot of the current game state, including:
     * <ul>
     *   <li>Board layout with piece positions</li>
     *   <li>Possible moves for selected pieces</li>
     *   <li>Eliminated pieces for each player</li>
     *   <li>Game status (ongoing/completed)</li>
     * </ul>
     *
     * @return GameState object containing the complete game state
     * @see GameState
     */
    GameState getBoard();

    /**
     * Processes a player's click on the game board.
     * 
     * <p>This method handles all player interactions with the board, including:
     * <ul>
     *   <li>Selecting a piece to move</li>
     *   <li>Showing possible moves</li>
     *   <li>Executing moves</li>
     *   <li>Capturing pieces</li>
     * </ul>
     *
     * @param squareLabel The chess notation label of the clicked square (e.g., "e4" or "e2-e4")
     * @return Updated GameState after processing the click
     * @see GameState
     */
    GameState onClick(String squareLabel);

    /**
     * Gets the color of the player whose turn it currently is.
     * 
     * <p>In this 3-player chess variant, the turn order is:
     * <ol>
     *   <li>WHITE</li>
     *   <li>BLACK</li>
     * </ol>
     *
     * @return The Colour enum representing the current player's turn
     * @see Colour
     */
    Colour getTurn();
}
