package helper;

/**
 * Represents the two player colors in the chess game.
 * 
 * <p>This enum defines the two possible colors for chess pieces and players:
 * <ul>
 *   <li>WHITE - The player who moves first</li>
 *   <li>BLACK - The player who moves second</li>
 * </ul>
 * 
 * <p>The color also determines:
 * <ul>
 *   <li>Piece movement direction (WHITE moves up, BLACK moves down)</li>
 *   <li>Color space for position calculations</li>
 *   <li>Turn order</li>
 *   <li>Piece ownership</li>
 * </ul>
 * 
 * @see Position
 * @see BasePiece
 * @version 1.0
 */
public enum Colour {
    /** White player (moves first) */
    WHITE,
    /** Black player (moves second) */
    BLACK;

    /**
     * Gets the next player's color in turn order.
     * 
     * <p>This method implements the alternating turn sequence:
     * <ul>
     *   <li>WHITE -> BLACK</li>
     *   <li>BLACK -> WHITE</li>
     * </ul>
     * 
     * @return Color of the next player
     */
    public Colour next() {
        return this == WHITE ? BLACK : WHITE;
    }

    /**
     * Converts color to its string representation.
     * 
     * <p>Used for:
     * <ul>
     *   <li>Piece notation (e.g., "WK" for White King)</li>
     *   <li>Web interface display</li>
     *   <li>Game state serialization</li>
     * </ul>
     * 
     * @return "W" for WHITE, "B" for BLACK
     */
    @Override
    public String toString() {
        return this == WHITE ? "W" : "B";
    }
}

