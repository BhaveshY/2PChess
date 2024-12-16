package helper;

import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Represents the state of the game, including the board, possible moves, and eliminated pieces.
 */
public class GameState {
    private Map<String, String> board;
    private List<String> possibleMoves;
    private List<String> highlightSquares;
    private boolean gameOver;
    private String winner;
    private List<String> eliminatedWhitePieces;
    private List<String> eliminatedBlackPieces;

    public GameState() {
        // Default constructor
        this.highlightSquares = Collections.emptyList();
    }

    public GameState(Map<String, String> board, List<String> possibleMoves) {
        this.board = board;
        this.possibleMoves = possibleMoves;
        this.gameOver = false;
        this.winner = null;
        this.eliminatedWhitePieces = Collections.emptyList();
        this.eliminatedBlackPieces = Collections.emptyList();
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public List<String> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(List<String> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver, String winner) {
        this.gameOver = gameOver;
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<String> getEliminatedWhitePieces() {
        return eliminatedWhitePieces;
    }

    public void setEliminatedWhitePieces(List<String> eliminatedWhitePieces) {
        this.eliminatedWhitePieces = eliminatedWhitePieces;
    }

    public List<String> getEliminatedBlackPieces() {
        return eliminatedBlackPieces;
    }

    public void setEliminatedBlackPieces(List<String> eliminatedBlackPieces) {
        this.eliminatedBlackPieces = eliminatedBlackPieces;
    }

    public List<String> getHighlightSquares() {
        return highlightSquares;
    }

    public void setHighlightSquares(List<String> highlightSquares) {
        this.highlightSquares = highlightSquares;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "board=" + board +
                ", possibleMoves=" + possibleMoves +
                ", gameOver=" + gameOver +
                ", winner='" + winner + '\'' +
                ", eliminatedWhitePieces=" + eliminatedWhitePieces +
                ", eliminatedBlackPieces=" + eliminatedBlackPieces +
                ", highlightSquares=" + highlightSquares +
                '}';
    }
}