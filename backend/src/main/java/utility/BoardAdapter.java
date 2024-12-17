package utility;

import helper.Colour;
import helper.InvalidPositionException;
import helper.Position;
import helper.GameState;
import entity.BasePiece;
import entity.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adapter class for converting between internal board representation and web interface format.
 * 
 * <p>This class implements the Adapter design pattern to bridge the gap between:
 * <ul>
 *   <li>Internal game model (Position-based)</li>
 *   <li>Web interface model (String-based)</li>
 * </ul>
 * 
 * <p>Key responsibilities:
 * <ul>
 *   <li>Board state conversion</li>
 *   <li>Position notation translation</li>
 *   <li>Move validation</li>
 *   <li>Game state serialization</li>
 * </ul>
 * 
 * @see Board
 * @see GameState
 * @see Position
 * @version 1.0
 */
public class BoardAdapter {

    /**
     * Converts internal board state to web interface game state.
     * 
     * <p>This method handles:
     * <ul>
     *   <li>Board layout conversion</li>
     *   <li>Possible moves calculation</li>
     *   <li>Game status tracking</li>
     *   <li>Eliminated pieces tracking</li>
     * </ul>
     * 
     * @param board The current game board
     * @return GameState object for web interface
     */
    public static GameState convertModelBoardToGameState(Board board) {
        GameState gameState = new GameState();

        Map<String, String> viewBoard = convertModelBoardToViewBoard(board.getBoardMap());
        gameState.setBoard(viewBoard);

        gameState.setPossibleMoves(Collections.emptyList());
        gameState.setGameOver(board.isGameOver(), board.getWinner());

        Map<String, List<String>> eliminatedPieces = board.getEliminatedPieces();
        gameState.setEliminatedWhitePieces(eliminatedPieces.getOrDefault("white", Collections.emptyList()));
        gameState.setEliminatedBlackPieces(eliminatedPieces.getOrDefault("black", Collections.emptyList()));

        return gameState;
    }

    /**
     * Converts internal board map to web interface format.
     * 
     * <p>This method:
     * <ul>
     *   <li>Translates positions to algebraic notation</li>
     *   <li>Handles dual color space mapping</li>
     *   <li>Resolves piece conflicts in same physical square</li>
     * </ul>
     * 
     * @param modelBoard Internal board representation
     * @return Map of square notation to piece representation
     */
    public static Map<String, String> convertModelBoardToViewBoard(Map<Position, BasePiece> modelBoard) {
        Map<String, String> viewBoard = new HashMap<>();

        for(Position position: modelBoard.keySet()) {
            BasePiece piece = modelBoard.get(position);
            if(piece != null) {
                String posStr = String.format("%c%d", 
                    (char)('a' + position.getColumn()), 
                    8 - position.getRow());
                
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
     * Converts a set of positions to web interface square notation.
     * 
     * <p>Used for:
     * <ul>
     *   <li>Highlighting possible moves</li>
     *   <li>Showing selected squares</li>
     *   <li>Marking special moves</li>
     * </ul>
     * 
     * @param possibleMoves Set of positions to convert
     * @return List of squares in algebraic notation
     */
    public static List<String> convertHighlightSquaresToViewBoard(Set<Position> possibleMoves) {
        List<String> moves = new ArrayList<>();
        if(possibleMoves == null) {
            return Collections.emptyList();
        }
        for(Position pos: possibleMoves) {
            String posStr = String.format("%c%d", 
                (char)('a' + pos.getColumn()), 
                8 - pos.getRow());
            moves.add(posStr);
        }

        return moves;
    }

    /**
     * Converts algebraic notation to internal position.
     * 
     * <p>This method:
     * <ul>
     *   <li>Validates notation format</li>
     *   <li>Converts to internal coordinates</li>
     *   <li>Creates position in specified color space</li>
     * </ul>
     * 
     * @param square Square in algebraic notation (e.g., "e4")
     * @param colour Color space for the position
     * @return Internal position representation
     * @throws InvalidPositionException if notation is invalid
     */
    public static Position getPositionFromNotation(String square, Colour colour) throws InvalidPositionException {
        if(square == null || square.length() != 2 || !Character.isAlphabetic(square.charAt(0)) || !Character.isDigit(square.charAt(1))) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        char column = Character.toLowerCase(square.charAt(0));
        int row = 8 - (square.charAt(1) - '0');
        if(column < 'a' || column > 'h' || row < 0 || row > 7) {
            throw new InvalidPositionException("Invalid String position: " + square);
        }

        int columnIndex = column - 'a';
        return Position.get(colour, row, columnIndex);
    }

    /**
     * Calculates unique identifier for a board square.
     * 
     * <p>Used for:
     * <ul>
     *   <li>Square identification in UI</li>
     *   <li>Move validation</li>
     *   <li>Board state tracking</li>
     * </ul>
     * 
     * @param square Square in algebraic notation
     * @return Unique square identifier (0-63)
     * @throws InvalidPositionException if notation is invalid
     */
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
