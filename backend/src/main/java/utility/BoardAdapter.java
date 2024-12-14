package utility;

import common.Colour;
import common.InvalidPositionException;
import common.Position;
import common.GameState;
import model.BasePiece;
import model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Class BoardAdapter to convert information for web app representable form
 **/
public class BoardAdapter {

    /**
     *  Method to convert board data to a GameState object for the webapp
     * @param board The board instance representing the current game state
     * @return GameState containing board layout, possible moves, and eliminated pieces
     **/
    public static GameState convertModelBoardToGameState(Board board) {
        GameState gameState = new GameState();

        // Convert the board map to a string map
        Map<String, String> viewBoard = convertModelBoardToViewBoard(board.getBoardMap());
        gameState.setBoard(viewBoard);

        // Get the current player's possible moves if any
        // (Assuming you want to include possible moves based on some logic)
        // This part depends on how you manage selected pieces and their moves
        // For simplicity, we'll leave it empty here
        gameState.setPossibleMoves(Collections.emptyList());

        // Set game over status and winner
        gameState.setGameOver(board.isGameOver(), board.getWinner());

        // Set eliminated pieces
        Map<String, List<String>> eliminatedPieces = board.getEliminatedPieces();
        gameState.setEliminatedWhitePieces(eliminatedPieces.getOrDefault("white", List.of()));
        gameState.setEliminatedBlackPieces(eliminatedPieces.getOrDefault("black", List.of()));

        return gameState;
    }

    /**
     *  Method to convert board data to Map of Strings for webapp
     * @param modelBoard a map of position and piece as input
     * @return Map of String and String
     **/
    public static Map<String, String> convertModelBoardToViewBoard(Map<Position, BasePiece> modelBoard) {
        Map<String, String> viewBoard = new HashMap<>();

        for(Position position: modelBoard.keySet()) {
            BasePiece piece = modelBoard.get(position);
            if(piece != null) {
                // Convert to standard chess notation (e.g., "e2")
                // Note: Both color spaces use the same standard notation for the same physical square
                String posStr = String.format("%c%d", 
                    (char)('a' + position.getColumn()), 
                    8 - position.getRow());  // Invert row for standard notation
                
                // If there's already a piece at this position in the view board,
                // it means we have pieces in both color spaces at the same physical location
                // In this case, we keep the piece that matches the current turn
                String existingPiece = viewBoard.get(posStr);
                if (existingPiece == null || 
                    (piece.getColour() == Colour.WHITE && existingPiece.startsWith("B")) ||
                    (piece.getColour() == Colour.BLACK && existingPiece.startsWith("W"))) {
                    viewBoard.put(posStr, piece.toString());
                }
            }
        }

        return viewBoard;
    }

    /**
     *  Method to convert list of positions to highlight to list of strings
     * @param possibleMoves a list of positions to highlight
     * @return list of strings
     **/
    public static List<String> convertHighlightSquaresToViewBoard(Set<Position> possibleMoves) {
        List<String> moves = new ArrayList<>();
        if(possibleMoves == null) {
            return Collections.emptyList();
        }
        for(Position pos: possibleMoves) {
            // Convert to standard chess notation (e.g., "e2")
            String posStr = String.format("%c%d", 
                (char)('a' + pos.getColumn()), 
                8 - pos.getRow());  // Invert row for standard notation
            moves.add(posStr);
        }

        return moves;
    }

    /**
     * Convert standard chess notation to Position
     * @param square The standard chess notation (e.g., "e2")
     * @return Position object
     */
    public static Position getPositionFromNotation(String square, Colour colour) throws InvalidPositionException {
        if(square == null || square.length() != 2 || !Character.isAlphabetic(square.charAt(0)) || !Character.isDigit(square.charAt(1))) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        char column = Character.toLowerCase(square.charAt(0));
        int row = 8 - (square.charAt(1) - '0');  // Convert to 0-7 range
        if(column < 'a' || column > 'h' || row < 0 || row > 7) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        int columnIndex = column - 'a';
        return Position.get(colour, row, columnIndex);
    }

    /**
     * Calculates unique ID for each square based on label
     * @param  square The unique label of the square which is clicked by player
     * @return unique ID
     * */
    public static int calculateSquareId(String square) throws InvalidPositionException {
        if(square == null || square.length() != 2 || !Character.isAlphabetic(square.charAt(0)) || !Character.isDigit(square.charAt(1))) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        char column = Character.toLowerCase(square.charAt(0));
        int row = square.charAt(1) - '1';
        if(column < 'a' || column > 'h' || row < 0 || row > 7) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        int columnIndex = column - 'a';
        return row * 8 + columnIndex;
    }
}
