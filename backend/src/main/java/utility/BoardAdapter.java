package utility;

import common.InvalidPositionException;
import common.Position;
import common.Colour;
import model.BasePiece;

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
                String posStr = String.format("%c%d", 
                    (char)('a' + position.getColumn()), 
                    8 - position.getRow());  // Invert row for standard notation
                viewBoard.put(posStr, piece.toString());
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
