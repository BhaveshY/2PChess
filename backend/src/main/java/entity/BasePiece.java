package entity;

import helper.Colour;
import helper.Direction;
import helper.Position;

import java.util.Map;
import java.util.Set;

/**
 * Base class for all chess pieces in the game.
 * 
 * <p>This abstract class provides the foundation for all chess pieces,
 * implementing common functionality and enforcing consistent behavior
 * across different piece types. Each piece type (King, Queen, Rook, etc.)
 * extends this class to define its specific movement patterns.
 * 
 * <p>Features:
 * <ul>
 *   <li>Common piece attributes (color, position)</li>
 *   <li>Movement pattern framework</li>
 *   <li>Capture rules</li>
 *   <li>Position validation</li>
 * </ul>
 * 
 * Abstract Base class for all chess pieces following Liskov Substitution Principle.
 * Each subclass must be able to substitute BasePiece without breaking the application.
 * 
 * @see Colour
 * @see Direction
 * @see Position
 * @version 1.0
 */
public abstract class BasePiece {

    /** Color of the chess piece [WHITE, BLACK] */
    protected final Colour colour;
    
    /** List of possible directions a piece can move */
    protected Direction[][] directions;

    /**
     * Creates a new chess piece with the specified color.
     * 
     * <p>Initializes the piece with its color and sets up its
     * movement directions through the abstract setupDirections method.
     * 
     * @param colour Color of the chess piece being initiated
     */
    public BasePiece(Colour colour) {
        this.colour = colour;
        setupDirections();
    }

    /**
     * Initializes the movement directions for this piece type.
     * 
     * <p>Each piece type must define its own movement pattern by
     * implementing this method. The directions array defines all
     * possible directions the piece can move.
     */
    protected abstract void setupDirections();

    /**
     * Gets the color of this piece.
     * 
     * <p>Made final to ensure consistent behavior across all piece types,
     * following LSP.
     * 
     * @return Color of the piece
     */
    public final Colour getColour() {
        return colour;
    }

    /**
     * Gets the movement directions for this piece.
     * 
     * @return Array of direction arrays representing possible movement patterns
     */
    public Direction[][] getDirections() {
        return directions;
    }

    /**
     * Calculates all possible moves for this piece from its current position.
     * 
     * <p>Each piece type must implement its own movement rules by considering:
     * <ul>
     *   <li>The piece's movement pattern</li>
     *   <li>Current board state</li>
     *   <li>Other pieces blocking movement</li>
     *   <li>Board boundaries</li>
     * </ul>
     * 
     * @param boardMap Current state of the board
     * @param start Starting position of the piece
     * @return Set of positions this piece can move to
     */
    public abstract Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start);

    /**
     * Gets positions to highlight for possible moves.
     * 
     * <p>Can be overridden by pieces that need special highlighting
     * (like King for castling). By default, returns the same as
     * getPossibleMoves.
     * 
     * @param boardMap Current state of the board
     * @param start Starting position of the piece
     * @return Set of positions to highlight
     */
    public Set<Position> getHighlightPolygons(Map<Position, BasePiece> boardMap, Position start) {
        return getPossibleMoves(boardMap, start);
    }

    /**
     * Checks if this piece can move to a specific position.
     * 
     * <p>This method ensures consistent move validation across all piece types.
     * Made final to enforce LSP.
     * 
     * @param boardMap Current state of the board
     * @param start Starting position
     * @param end Target position
     * @return true if the move is valid, false otherwise
     */
    public final boolean canMoveTo(Map<Position, BasePiece> boardMap, Position start, Position end) {
        return getPossibleMoves(boardMap, start).contains(end);
    }

    /**
     * Returns a string representation of this piece.
     * 
     * <p>Format: [Color][First letter of piece type]
     * Example: "WK" for White King
     * 
     * @return String representation of the piece
     */
    @Override
    public String toString() {
        return this.colour.toString() + getClass().getSimpleName().charAt(0);
    }
}