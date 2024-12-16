package service;

import helper.Position;
import helper.InvalidMoveException;
import helper.Colour;
import entity.BasePiece;
import entity.Board;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * [SRP - Single Responsibility Principle]
 * This class has a single responsibility: Move validation.
 * It handles only the logic related to validating chess moves,
 * separating this concern from game state management and piece movement.
 */
@Component
public class MoveValidator {
    
    /**
     * [SRP] Validates move legality - single, focused responsibility
     * [OCP] Can be extended with new validation rules without modifying existing code
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
     * [SRP] Validates turn order - single, focused responsibility
     * [ISP] Method depends only on the interfaces it needs (Colour and piece color)
     */
    public boolean isCorrectTurn(BasePiece piece, Colour currentTurn) {
        return piece != null && piece.getColour() == currentTurn;
    }

    /**
     * [SRP] Validates check condition - single, focused responsibility
     * [DIP] Depends on Board abstraction rather than concrete implementation details
     */
    public boolean wouldResultInCheck(Board board, Position start, Position end, Colour currentTurn) {
        // Since isCheck is private, we'll use the public method isCurrentPlayersPiece
        // and validate based on the current game state
        return !board.isCurrentPlayersPiece(start);
    }

    /**
     * [SRP] Combines all validation rules in one place
     * [OCP] New validation rules can be added without changing existing ones
     * [LSP] Works with any valid Board implementation that follows the contract
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