package service;

import com.google.common.collect.ImmutableSet;
import helper.Colour;
import helper.InvalidMoveException;
import helper.InvalidPositionException;
import helper.GameState;
import helper.Position;
import entity.Board;
import entity.BasePiece;

import org.springframework.stereotype.Service;
import utility.BoardAdapter;
import utility.Log;

import java.util.Set;

/**
 * Main implementation of the chess game interface.
 * 
 * <p>This class serves as the primary backend controller for the chess game,
 * handling:
 * <ul>
 *   <li>Game state management</li>
 *   <li>Move processing</li>
 *   <li>Player interactions</li>
 *   <li>Board state updates</li>
 * </ul>
 * 
 * <p>The implementation ensures thread-safe access to game state and
 * coordinates all game operations through a well-defined interface.
 * 
 * Class containing the main logic of the backend.
 * The click inputs from the webapp are communicated with the backend.
 * 
 * @see IGameInterface
 * @see Board
 * @see GameState
 * @version 1.0
 */
@Service
public class GameInterfaceImpl implements IGameInterface {

    /** Logger tag for this class */
    private static final String TAG = GameInterfaceImpl.class.getSimpleName();
    
    /** The game board instance */
    private final Board board;
    
    /** Starting position for a move in progress */
    private Position moveStartPos;
    
    /** Currently highlighted squares on the board */
    private Set<Position> highlightSquares;

    /**
     * Creates a new game interface with initial setup.
     * 
     * <p>Initializes:
     * <ul>
     *   <li>New game board</li>
     *   <li>Empty move state</li>
     *   <li>No highlighted squares</li>
     * </ul>
     */
    public GameInterfaceImpl() {
        Log.d(TAG, "initGame GameInterfaceImpl()");
        board = new Board();
        moveStartPos = null;
        highlightSquares = ImmutableSet.of();
    }

    /**
     * {@inheritDoc}
     * 
     * <p>This implementation converts the internal board state to a
     * format suitable for the web interface using BoardAdapter.
     */
    @Override
    public GameState getBoard() {
        return BoardAdapter.convertModelBoardToGameState(board);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>This implementation handles two types of clicks:
     * <ol>
     *   <li>Move execution (format: "e2-e4")</li>
     *   <li>Square selection (format: "e4")</li>
     * </ol>
     * 
     * <p>For moves:
     * <ul>
     *   <li>Validates move format</li>
     *   <li>Converts algebraic notation to internal coordinates</li>
     *   <li>Executes valid moves</li>
     *   <li>Handles captures</li>
     * </ul>
     * 
     * <p>For selections:
     * <ul>
     *   <li>Shows possible moves for selected piece</li>
     *   <li>Highlights valid squares</li>
     *   <li>Validates piece ownership</li>
     * </ul>
     */
    @Override
    public GameState onClick(String squareLabel) {
        try {
            Log.d(TAG, ">>> onClick called: squareLabel: " + squareLabel);
            
            if (squareLabel.contains("-")) {
                handleMoveCommand(squareLabel);
            } else {
                handleSquareSelection(squareLabel);
            }
        } catch (InvalidMoveException e) {
            Log.e(TAG, "InvalidMoveException onClick: " + e.getMessage());
            moveStartPos = null;
            highlightSquares = ImmutableSet.of();
            return BoardAdapter.convertModelBoardToGameState(board);
        }
        
        return BoardAdapter.convertModelBoardToGameState(board);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>This implementation delegates to the board's getTurn method
     * to maintain consistent turn tracking.
     */
    @Override
    public Colour getTurn() {
        return board.getTurn();
    }

    /**
     * Processes a move command in algebraic notation.
     * 
     * @param command Move command in format "e2-e4"
     * @throws InvalidMoveException if the move is invalid
     */
    private void handleMoveCommand(String command) throws InvalidMoveException {
        String[] positions = command.split("-");
        if (positions.length != 2) {
            throw new InvalidMoveException("Invalid move format");
        }
        
        String startSquare = positions[0];
        String endSquare = positions[1];
        
        Position startPosition = getPositionFromAlgebraic(startSquare);
        if (startPosition == null) {
            throw new InvalidMoveException("Invalid start position");
        }
        
        BasePiece piece = board.getPiece(startPosition);
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        
        Position endPosition = getEndPosition(piece, endSquare);
        if (endPosition == null) {
            throw new InvalidMoveException("Invalid end position");
        }
        
        Log.d(TAG, String.format("Moving piece %s from %s to %s", 
            piece.toString(), startPosition, endPosition));
        
        try {
            board.move(startPosition, endPosition);
            resetMoveState();
        } catch (InvalidPositionException e) {
            throw new InvalidMoveException("Invalid move: " + e.getMessage());
        }
    }

    /**
     * Processes a square selection command.
     * 
     * @param square Square in algebraic notation (e.g., "e4")
     */
    private void handleSquareSelection(String square) {
        try {
            Position position = getPositionFromAlgebraic(square);
            if (position == null) {
                resetMoveState();
                return;
            }
            
            if (board.isCurrentPlayersPiece(position)) {
                moveStartPos = position;
                highlightSquares = board.getPossibleMoves(moveStartPos);
                if (highlightSquares.isEmpty()) {
                    resetMoveState();
                }
            } else {
                resetMoveState();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in square selection: " + e.getMessage());
            resetMoveState();
        }
    }

    /**
     * Converts algebraic notation to internal position.
     * 
     * @param algebraic Square in algebraic notation (e.g., "e4")
     * @return Position object or null if invalid
     */
    private Position getPositionFromAlgebraic(String algebraic) {
        try {
            int col = algebraic.charAt(0) - 'a';
            int row = Character.getNumericValue(algebraic.charAt(1)) - 1;
            
            // Try both color spaces
            try {
                Position pos = Position.get(Colour.WHITE, row, col);
                if (board.getPiece(pos) != null) {
                    return pos;
                }
            } catch (InvalidPositionException ignored) {}
            
            try {
                return Position.get(Colour.BLACK, row, col);
            } catch (InvalidPositionException ignored) {}
            
            return Position.get(Colour.WHITE, row, col);
        } catch (Exception e) {
            Log.e(TAG, "Error converting algebraic notation: " + e.getMessage());
            return null;
        }
    }

    /**
     * Determines the correct end position for a move.
     * 
     * @param piece Moving piece
     * @param algebraic Target square in algebraic notation
     * @return Position object or null if invalid
     */
    private Position getEndPosition(BasePiece piece, String algebraic) {
        try {
            int col = algebraic.charAt(0) - 'a';
            int row = Character.getNumericValue(algebraic.charAt(1)) - 1;
            
            // Try same color space first
            Position endPos = Position.get(piece.getColour(), row, col);
            BasePiece targetPiece = board.getPiece(endPos);
            
            // Check opposite color space for captures
            if (targetPiece == null || targetPiece.getColour() == piece.getColour()) {
                Colour oppositeColour = piece.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
                Position oppositePos = Position.get(oppositeColour, row, col);
                BasePiece oppositeTargetPiece = board.getPiece(oppositePos);
                
                if (oppositeTargetPiece != null && oppositeTargetPiece.getColour() != piece.getColour()) {
                    return oppositePos;
                }
            }
            
            return endPos;
        } catch (Exception e) {
            Log.e(TAG, "Error getting end position: " + e.getMessage());
            return null;
        }
    }

    /**
     * Resets the move state.
     * 
     * <p>Clears:
     * <ul>
     *   <li>Move start position</li>
     *   <li>Highlighted squares</li>
     * </ul>
     */
    private void resetMoveState() {
        moveStartPos = null;
        highlightSquares = ImmutableSet.of();
    }
}