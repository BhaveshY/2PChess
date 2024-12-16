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
 * PieceFactory - helper class to create objects chess pieces
 **/
public class PieceFactory {

    /**
     * createPiece - based on the type and colour, creates a chess piece
     * @param colour - piece colour
     * @param type - piece name e.g. king
     * @return BasePiece
     **/
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
