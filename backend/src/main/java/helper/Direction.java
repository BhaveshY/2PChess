package helper;

/**
 * Represents the possible movement directions for chess pieces.
 * 
 * <p>This enum defines the four cardinal directions that pieces can move in:
 * <ul>
 *   <li>FORWARD - Towards opponent's side (decreasing row for WHITE, increasing for BLACK)</li>
 *   <li>BACKWARD - Towards own side (increasing row for WHITE, decreasing for BLACK)</li>
 *   <li>LEFT - Towards the a-file (decreasing column)</li>
 *   <li>RIGHT - Towards the h-file (increasing column)</li>
 * </ul>
 * 
 * <p>The directions are relative to each piece's color, meaning FORWARD
 * moves up the board for WHITE pieces and down for BLACK pieces.
 * 
 * <p>These basic directions can be combined to create diagonal and complex
 * movements for different piece types.
 * 
 * @see Position
 * @see BasePiece
 * @version 1.0
 */
public enum Direction {
    /** Move towards opponent's side */
    FORWARD,
    /** Move towards own side */
    BACKWARD,
    /** Move towards a-file */
    LEFT,
    /** Move towards h-file */
    RIGHT;

    /**
     * Converts direction to its string representation.
     * 
     * <p>Used primarily for:
     * <ul>
     *   <li>Debugging output</li>
     *   <li>Web interface communication</li>
     *   <li>Move logging</li>
     * </ul>
     * 
     * @return String representation of the direction
     */
    @Override
    public String toString() {
        switch (this) {
            case FORWARD: return "FORWARD";
            case BACKWARD: return "BACKWARD";
            case LEFT: return "LEFT";
            default:case RIGHT: return "RIGHT";
        }
    }
}
