package main;

import abstraction.IGameInterface;
import com.google.common.collect.ImmutableSet;
import common.Colour;
import common.InvalidMoveException;
import common.InvalidPositionException;
import common.GameState;
import common.Position;
import model.Board;
import model.BasePiece;
import utility.BoardAdapter;
import utility.Log;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class containing the main logic of the backend.
 * the click inputs from the webapp is communicated with the backend.
 */
public class GameMain implements IGameInterface {

    private static final String TAG = GameMain.class.getSimpleName();
    private final Board board;
    private Position moveStartPos;
    private Set<Position> highlightSquares;

    /**
     * GameMain Constructor. Entry point to the backend logic
     * */
    public GameMain() {
        Log.d(TAG, "initGame GameMain()");
        board = new Board();
        moveStartPos = null;
        highlightSquares = ImmutableSet.of();
    }

    /**
     * Get the current board map being used by backend for current game session
     * @return Board map
     * */
    @Override
    public Map<String, String> getBoard() {
        return board.getWebViewBoard();
    }

    /**
     * Responsible for sending mouse click events to backend and apply game logic over it to display
     * updated board layout to player.
     * @param  squareLabel The unique label of the square which is clicked by player
     * @return GameState which contains current game board layout and list of squares to highlight
     **/
    @Override
    public GameState onClick(String squareLabel) {
        try {
            Log.d(TAG, ">>> onClick called: squareLabel: " + squareLabel);
            
            // Check if this is a move command (contains a hyphen)
            if (squareLabel.contains("-")) {
                String[] positions = squareLabel.split("-");
                if (positions.length != 2) {
                    throw new InvalidMoveException("Invalid move format");
                }
                
                String startSquare = positions[0];
                String endSquare = positions[1];
                
                // Convert algebraic notation (e.g., "e2") to internal coordinates
                int startCol = startSquare.charAt(0) - 'a';
                int startRow = Character.getNumericValue(startSquare.charAt(1)) - 1;
                
                int endCol = endSquare.charAt(0) - 'a';
                int endRow = Character.getNumericValue(endSquare.charAt(1)) - 1;
                
                Log.d(TAG, String.format("Attempting move from %s (%d,%d) to %s (%d,%d)", 
                    startSquare, startRow, startCol, endSquare, endRow, endCol));
                
                // Try both White and Black positions to find the piece
                Position startPosition = null;
                BasePiece piece = null;
                
                try {
                    startPosition = Position.get(Colour.WHITE, startRow, startCol);
                    piece = board.getPiece(startPosition);
                    if (piece == null) {
                        startPosition = Position.get(Colour.BLACK, startRow, startCol);
                        piece = board.getPiece(startPosition);
                    }
                } catch (InvalidPositionException e) {
                    try {
                        startPosition = Position.get(Colour.BLACK, startRow, startCol);
                        piece = board.getPiece(startPosition);
                    } catch (InvalidPositionException e2) {
                        throw new InvalidMoveException("Invalid start position");
                    }
                }
                
                if (piece == null) {
                    throw new InvalidMoveException("No piece at start position");
                }
                
                // Use the same color for end position as the piece we found
                Position endPosition;
                try {
                    endPosition = Position.get(piece.getColour(), endRow, endCol);
                } catch (InvalidPositionException e) {
                    throw new InvalidMoveException("Invalid end position");
                }
                
                Log.d(TAG, String.format("Moving piece %s from %s to %s", 
                    piece.toString(), startPosition, endPosition));
                
                try {
                    // Attempt the move
                    board.move(startPosition, endPosition);
                } catch (InvalidPositionException e) {
                    throw new InvalidMoveException("Invalid move: " + e.getMessage());
                }
                
                // Reset state after move
                moveStartPos = null;
                highlightSquares = ImmutableSet.of();
            } else {
                // Single square click - show possible moves
                int col = squareLabel.charAt(0) - 'a';
                int row = Character.getNumericValue(squareLabel.charAt(1)) - 1;
                
                Log.d(TAG, String.format("Clicked square %s at position (%d,%d)", 
                    squareLabel, row, col));
                
                // Try both White and Black positions
                Position position = null;
                BasePiece piece = null;
                
                try {
                    position = Position.get(Colour.WHITE, row, col);
                    piece = board.getPiece(position);
                    if (piece == null) {
                        position = Position.get(Colour.BLACK, row, col);
                        piece = board.getPiece(position);
                    }
                } catch (InvalidPositionException e) {
                    try {
                        position = Position.get(Colour.BLACK, row, col);
                        piece = board.getPiece(position);
                    } catch (InvalidPositionException e2) {
                        // If both positions are invalid, use White position as default
                        try {
                            position = Position.get(Colour.WHITE, row, col);
                        } catch (InvalidPositionException e3) {
                            Log.e(TAG, "Could not create valid position");
                            return new GameState(getBoard(), List.of());
                        }
                    }
                }
                
                Log.d(TAG, String.format("Found piece %s at position %s", 
                    piece != null ? piece.toString() : "null", position));
                
                if (board.isCurrentPlayersPiece(position)) {
                    moveStartPos = position;
                    Log.d(TAG, ">>> Selected piece at: " + moveStartPos);
                    highlightSquares = board.getPossibleMoves(moveStartPos);
                    Log.d(TAG, ">>> Possible moves: " + highlightSquares);
                    if (highlightSquares.isEmpty()) {
                        Log.d(TAG, ">>> No valid moves available");
                        moveStartPos = null;
                        highlightSquares = ImmutableSet.of();
                    }
                } else {
                    Log.d(TAG, ">>> Not current player's piece or no piece at position");
                    moveStartPos = null;
                    highlightSquares = ImmutableSet.of();
                }
            }
        } catch (InvalidMoveException e) {
            Log.e(TAG, "InvalidMoveException onClick: " + e.getMessage());
            moveStartPos = null;
            highlightSquares = ImmutableSet.of();
        }
        
        List<String> highlightSquaresList = BoardAdapter.convertHighlightSquaresToViewBoard(highlightSquares);
        GameState clickResponse = new GameState(getBoard(), highlightSquaresList);
        if (board.isGameOver()) {
            String winner = board.getWinner();
            Log.d(TAG, "Winner: " + winner);
            clickResponse.setGameOver(winner);
        }
        Log.d(TAG, "ClickResponse: " + clickResponse);
        return clickResponse;
    }

    /**
     * @return returns which colour turn it is currently
     * */
    @Override
    public Colour getTurn() {
        return board.getTurn();
    }
}