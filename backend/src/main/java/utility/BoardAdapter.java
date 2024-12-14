package utility;

import common.InvalidPositionException;
import model.BasePiece;
import common.Position;

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
                viewBoard.put(position.toString().substring(1), piece.toString());
            }
        }

        return  viewBoard;
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
        for(Position pi: possibleMoves) {
            moves.add(pi.toString());
        }

        return moves;
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
