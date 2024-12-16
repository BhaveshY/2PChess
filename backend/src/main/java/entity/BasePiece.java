package entity;

import helper.Colour;
import helper.Direction;
import helper.Position;

import java.util.Map;
import java.util.Set;

/**
 * Abstract Base class for all chess pieces following Liskov Substitution Principle.
 * Each subclass must be able to substitute BasePiece without breaking the application.
 */
public abstract class BasePiece {

    protected final Colour colour; // colour of the chess piece [WHITE, BLACK]
    protected Direction[][] directions; // List of possible directions a piece can move

    /**
     * BasePiece constructor
     * @param colour: Colour of the chess piece being initiated
     */
    public BasePiece(Colour colour) {
        this.colour = colour;
        setupDirections();
    }

    /**
     * Method to initialize directions for a chess piece.
     * Each piece type must define its own movement pattern.
     */
    protected abstract void setupDirections();

    /**
     * Method to get the colour of the piece
     * @return Colour
     */
    public final Colour getColour() {  // Made final to ensure consistent behavior
        return colour;
    }

    /**
     * Get the movement directions for this piece
     * @return Array of direction arrays representing possible movement patterns
     */
    public Direction[][] getDirections() {
        return directions;
    }

    /**
     * Method to get possible moves for a piece.
     * Each piece type must implement its own movement rules.
     * @param boardMap: Current state of the board
     * @param start: Starting position of the piece
     * @return Set<Position>: Set of possible positions the piece can move to
     */
    public abstract Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start);

    /**
     * Method to get positions to highlight for possible moves.
     * Can be overridden by pieces that need special highlighting (like King for castling).
     * @param boardMap: Current state of the board
     * @param start: Starting position of the piece
     * @return Set<Position>: Set of positions to highlight
     */
    public Set<Position> getHighlightPolygons(Map<Position, BasePiece> boardMap, Position start) {
        return getPossibleMoves(boardMap, start);
    }

    /**
     * Checks if the piece can move to a specific position.
     * This method ensures consistent behavior across all piece types.
     */
    public final boolean canMoveTo(Map<Position, BasePiece> boardMap, Position start, Position end) {
        return getPossibleMoves(boardMap, start).contains(end);
    }

    /**
     * Returns string representation of the piece.
     * This method ensures consistent behavior across all piece types.
     */
    @Override
    public String toString() {
        return this.colour.toString() + getClass().getSimpleName().charAt(0);
    }
}