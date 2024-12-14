package abstraction;
import common.Colour;
import common.GameState;

public interface IGameInterface {

    /**
     * Get the current game state including the board and other relevant information.
     * @return GameState containing board layout, possible moves, and eliminated pieces.
     */
    GameState getBoard();

    /**
     * Handle a click event on the board.
     * @param squareLabel The unique label of the square which is clicked by player.
     * @return Updated GameState after processing the click.
     */
    GameState onClick(String squareLabel);

    /**
     * @return The current player's turn.
     */
    Colour getTurn();
}
