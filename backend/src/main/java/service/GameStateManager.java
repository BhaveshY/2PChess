package service;

import helper.Colour;
import helper.GameState;
import entity.Board;
import org.springframework.stereotype.Component;
import utility.BoardAdapter;

/**
 * Manages the game state and turn progression
 */
@Component
public class GameStateManager {
    private final Board board;
    private Colour currentTurn;

    public GameStateManager() {
        this.board = new Board();
        this.currentTurn = Colour.WHITE;
    }

    public GameState getCurrentState() {
        return BoardAdapter.convertModelBoardToGameState(board);
    }

    public Colour getCurrentTurn() {
        return currentTurn;
    }

    public void advanceTurn() {
        currentTurn = currentTurn.next();
    }

    public Board getBoard() {
        return board;
    }
}