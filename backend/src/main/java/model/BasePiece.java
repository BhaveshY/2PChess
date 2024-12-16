package model;

import helper.Colour;
import helper.Direction;
import helper.Position;

import java.util.Map;
import java.util.Set;

/**
 *  Abstract Base class for all chess pieces. All chess pieces must extend this class
 *  and provide implementation to abstract methods according to their rules.
 **/
public abstract class BasePiece {

    protected Colour colour; // colour of the chess piece [WHITE, BLACK, Green]
    protected Direction[][] directions; // List of possible directions a piece can move. [Left, Right, Forward, Backward]

    /**
     * BasePiece constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public BasePiece(Colour colour) {
        this.colour = colour;
        setupDirections();
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    protected abstract void setupDirections();

    /**
     * Method to get the colour of the piece
     * @return Colour
     **/
    public Colour getColour() {
        return colour;
    }

    /**
     * Method to get possible moves for a piece
     * @param boardMap: Current state of the board
     * @param start: Starting position of the piece
     * @return Set<Position>: Set of possible positions the piece can move to
     **/
    public abstract Set<Position> getPossibleMoves(Map<Position, BasePiece> boardMap, Position start);

    /**
     * Method to get positions to highlight for possible moves
     * @param boardMap: Current state of the board
     * @param start: Starting position of the piece
     * @return Set<Position>: Set of positions to highlight
     **/
    public Set<Position> getHighlightPolygons(Map<Position, BasePiece> boardMap, Position start) {
        return getPossibleMoves(boardMap, start);
    }
}