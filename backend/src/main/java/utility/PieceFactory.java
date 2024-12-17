package utility;
import helper.Colour;
import entity.BasePiece;
import entity.Bishop;
import entity.King;
import entity.Knight;
import entity.Pawn;
import entity.Queen;
import entity.Rook;

/**
 * Factory class for creating chess pieces.
 * 
 * <p>This class implements the Factory design pattern to centralize
 * chess piece creation. It provides a single point of control for:
 * <ul>
 *   <li>Piece instantiation</li>
 *   <li>Type validation</li>
 *   <li>Color assignment</li>
 * </ul>
 * 
 * <p>The factory supports creating all standard chess pieces:
 * <ul>
 *   <li>King</li>
 *   <li>Queen</li>
 *   <li>Bishop</li>
 *   <li>Knight</li>
 *   <li>Rook</li>
 *   <li>Pawn</li>
 * </ul>
 * 
 * @see BasePiece
 * @see Colour
 * @version 1.0
 */
public class PieceFactory {

    /**
     * Creates a new chess piece of the specified type and color.
     * 
     * <p>This method:
     * <ul>
     *   <li>Validates the piece type</li>
     *   <li>Creates the appropriate piece instance</li>
     *   <li>Initializes it with the specified color</li>
     * </ul>
     * 
     * @param type Type of piece to create ("king", "queen", etc.)
     * @param colour Color of the piece
     * @return New chess piece instance
     * @throws IllegalArgumentException if the piece type is invalid
     */
    public static BasePiece createPiece(String type, Colour colour) {
        switch (type.toLowerCase()) {
            case "bishop":
                return new Bishop(colour);
            case "queen":
                return new Queen(colour);
            case "king":
                return new King(colour);
            case "knight":
                return new Knight(colour);
            case "rook":
                return new Rook(colour);
            case "pawn":
                return new Pawn(colour);
            default:
                throw new IllegalArgumentException("Invalid chess piece type: " + type);
        }
    }
}
