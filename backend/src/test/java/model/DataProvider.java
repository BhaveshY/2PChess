package model;

import helper.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataProvider {

    /** Method Source for some JUnit-Tests to test the interactions with different pieces
     *
     * @return stream of standard chess pieces
     */
    static Stream<BasePiece> pieceProvider() {
        List<BasePiece> pieces = new ArrayList<>();

        for (Colour colour : Colour.values()) {  // Will iterate over WHITE and BLACK
            pieces.add(new Pawn(colour));
            pieces.add(new Rook(colour));
            pieces.add(new Knight(colour));
            pieces.add(new Bishop(colour));
            pieces.add(new Queen(colour));
            pieces.add(new King(colour));
        }

        return pieces.stream();
    }
}
