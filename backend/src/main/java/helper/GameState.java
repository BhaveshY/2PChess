package helper;

import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Represents the complete state of a chess game at any given moment.
 * 
 * <p>This class encapsulates all information needed to represent the current state
 * of a chess game, including:
 * <ul>
 *   <li>Board configuration</li>
 *   <li>Possible moves</li>
 *   <li>Game status</li>
 *   <li>Eliminated pieces</li>
 * </ul>
 * 
 * <p>The state is designed to be easily serializable for:
 * <ul>
 *   <li>Web interface communication</li>
 *   <li>Game state persistence</li>
 *   <li>State restoration</li>
 * </ul>
 * 
 * @see Board
 * @see Position
 * @see BasePiece
 * @version 1.0
 */
public class GameState {
    /** Current board state as map of positions to piece representations */
    private Map<String, String> board;
    
    /** List of valid moves for the currently selected piece */
    private List<String> possibleMoves;
    
    /** Squares to highlight on the board */
    private List<String> highlightSquares;
    
    /** Flag indicating if the game has ended */
    private boolean gameOver;
    
    /** The color of the winning player, if game is over */
    private String winner;
    
    /** List of eliminated white pieces */
    private List<String> eliminatedWhitePieces;
    
    /** List of eliminated black pieces */
    private List<String> eliminatedBlackPieces;

    /**
     * Creates a new GameState with default values.
     * 
     * <p>Initializes:
     * <ul>
     *   <li>Empty board</li>
     *   <li>No possible moves</li>
     *   <li>No highlighted squares</li>
     *   <li>Game not over</li>
     *   <li>No winner</li>
     *   <li>No eliminated pieces</li>
     * </ul>
     */
    public GameState() {
        this.highlightSquares = Collections.emptyList();
    }

    /**
     * Gets the current board configuration.
     * 
     * @return Map of square notations to piece representations
     */
    public Map<String, String> getBoard() {
        return board;
    }

    /**
     * Sets the current board configuration.
     * 
     * @param board Map of square notations to piece representations
     */
    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    /**
     * Gets the list of possible moves for selected piece.
     * 
     * @return List of valid target squares in algebraic notation
     */
    public List<String> getPossibleMoves() {
        return possibleMoves;
    }

    /**
     * Sets the list of possible moves for selected piece.
     * 
     * @param possibleMoves List of valid target squares
     */
    public void setPossibleMoves(List<String> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    /**
     * Gets the squares to highlight on the board.
     * 
     * @return List of squares to highlight
     */
    public List<String> getHighlightSquares() {
        return highlightSquares;
    }

    /**
     * Sets the squares to highlight on the board.
     * 
     * @param highlightSquares List of squares to highlight
     */
    public void setHighlightSquares(List<String> highlightSquares) {
        this.highlightSquares = highlightSquares;
    }

    /**
     * Checks if the game has ended.
     * 
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the winning player's color.
     * 
     * @return Color of winner, or null if game not over
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the game's end state and winner.
     * 
     * @param gameOver Whether the game has ended
     * @param winner Color of winning player
     */
    public void setGameOver(boolean gameOver, String winner) {
        this.gameOver = gameOver;
        this.winner = winner;
    }

    /**
     * Gets the list of eliminated white pieces.
     * 
     * @return List of captured white pieces
     */
    public List<String> getEliminatedWhitePieces() {
        return eliminatedWhitePieces;
    }

    /**
     * Sets the list of eliminated white pieces.
     * 
     * @param eliminatedWhitePieces List of captured white pieces
     */
    public void setEliminatedWhitePieces(List<String> eliminatedWhitePieces) {
        this.eliminatedWhitePieces = eliminatedWhitePieces;
    }

    /**
     * Gets the list of eliminated black pieces.
     * 
     * @return List of captured black pieces
     */
    public List<String> getEliminatedBlackPieces() {
        return eliminatedBlackPieces;
    }

    /**
     * Sets the list of eliminated black pieces.
     * 
     * @param eliminatedBlackPieces List of captured black pieces
     */
    public void setEliminatedBlackPieces(List<String> eliminatedBlackPieces) {
        this.eliminatedBlackPieces = eliminatedBlackPieces;
    }
}