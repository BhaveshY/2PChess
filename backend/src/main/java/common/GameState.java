package common;

import java.util.List;
import java.util.Map;

/**
 * Class representing the state of the game.
 */
public class GameState {
    private Map<String, String> board;
    private List<String> highlightSquares;
    private boolean gameOver;
    private String winner;

    public GameState(Map<String, String> board, List<String> highlightSquares) {
        this.board = board;
        this.highlightSquares = highlightSquares;
        this.gameOver = false;
        this.winner = null;
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public List<String> getHighlightSquares() {
        return highlightSquares;
    }

    public void setHighlightSquares(List<String> highlightSquares) {
        this.highlightSquares = highlightSquares;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(String winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "board=" + board +
                ", highlightSquares=" + highlightSquares +
                ", gameOver=" + gameOver +
                ", winner='" + winner + '\'' +
                '}';
    }
}