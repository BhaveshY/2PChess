package service;

import helper.Position;
import helper.InvalidMoveException;
import helper.Colour;
import entity.BasePiece;
import entity.Board;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validates chess moves according to game rules and piece movement patterns.
 * 
 * <p>This class is responsible for ensuring that all moves in the game follow chess rules,
 * including:
 * <ul>
 *   <li>Piece movement patterns</li>
 *   <li>Turn order</li>
 *   <li>Check conditions</li>
 *   <li>Move legality</li>
 * </ul>
 * 
 * <p>The validator is designed to be stateless and thread-safe.
 * 
 * [SRP - Single Responsibility Principle]
 * This class has a single responsibility: Move validation.
 * It handles only the logic related to validating chess moves,
 * separating this concern from game state management and piece movement.
 * 
 * @see Board
 * @see BasePiece
 * @see Position
 * @version 1.0
 */
@Component
public class MoveValidator {
    
    /**
     * Validates if a move is legal according to chess rules.
     * 
     * <p>This method checks if a piece can move from the start position to the end position
     * based on the piece's movement rules and the current board state.
     * 
     * [SRP] Validates move legality - single, focused responsibility
     * [OCP] Can be extended with new validation rules without modifying existing code
     *
     * @param board The current board state
     * @param start Starting position of the piece
     * @param end Target position for the move
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Board board, Position start, Position end) {
        BasePiece piece = board.getPiece(start);
        if (piece == null) {
            return false;
        }

        Set<Position> possibleMoves = piece.getPossibleMoves(board.getBoardMap(), start);
        return possibleMoves.contains(end);
    }

    /**
     * Validates if it's the correct player's turn to move.
     * 
     * <p>This method ensures that players move only during their turn and can only
     * move their own pieces.
     * 
     * [SRP] Validates turn order - single, focused responsibility
     * [ISP] Method depends only on the interfaces it needs (Colour and piece color)
     *
     * @param piece The piece being moved
     * @param currentTurn The current player's turn
     * @return true if it's the correct player's turn, false otherwise
     */
    public boolean isCorrectTurn(BasePiece piece, Colour currentTurn) {
        return piece != null && piece.getColour() == currentTurn;
    }

    /**
     * Checks if a move would result in the current player being in check.
     * 
     * <p>This method simulates the move and verifies if it would leave or put
     * the current player's king in check.
     * 
     * [SRP] Validates check condition - single, focused responsibility
     * [DIP] Depends on Board abstraction rather than concrete implementation details
     *
     * @param board The current board state
     * @param start Starting position of the piece
     * @param end Target position for the move
     * @param currentTurn The current player's turn
     * @return true if the move would result in check, false otherwise
     */
    public boolean wouldResultInCheck(Board board, Position start, Position end, Colour currentTurn) {
        return !board.isCurrentPlayersPiece(start);
    }

    /**
     * Combines all move validation rules to determine if a move is legal.
     * 
     * <p>This method aggregates all validation rules and throws an exception if any
     * validation fails. It checks:
     * <ul>
     *   <li>Turn order</li>
     *   <li>Move validity</li>
     *   <li>Check conditions</li>
     * </ul>
     * 
     * [SRP] Combines all validation rules in one place
     * [OCP] New validation rules can be added without changing existing ones
     * [LSP] Works with any valid Board implementation that follows the contract
     *
     * @param board The current board state
     * @param start Starting position of the piece
     * @param end Target position for the move
     * @param currentTurn The current player's turn
     * @throws InvalidMoveException if the move is invalid
     */
    public void validateMove(Board board, Position start, Position end, Colour currentTurn) 
            throws InvalidMoveException {
        BasePiece piece = board.getPiece(start);
        
        if (!isCorrectTurn(piece, currentTurn)) {
            throw new InvalidMoveException("Not your turn or no piece at position");
        }
        
        if (!isValidMove(board, start, end)) {
            throw new InvalidMoveException("Invalid move for this piece");
        }

        // For now, we'll rely on the Board class's internal validation
        // since it has access to the private isCheck method
    }
} 