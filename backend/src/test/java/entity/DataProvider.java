package entity;

import helper.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Test data provider for chess piece testing.
 * 
 * <p>This class provides test data for unit tests by:
 * <ul>
 *   <li>Creating instances of all chess piece types</li>
 *   <li>Generating pieces in both colors</li>
 *   <li>Supporting parameterized testing</li>
 * </ul>
 * 
 * <p>The provider ensures comprehensive testing by:
 * <ul>
 *   <li>Covering all piece types</li>
 *   <li>Testing both color variations</li>
 *   <li>Supporting different test scenarios</li>
 * </ul>
 * 
 * @see BasePiece
 * @see Colour
 * @version 1.0
 */
public class DataProvider {

    /**
     * Provides a stream of all standard chess pieces.
     * 
     * <p>Creates:
     * <ul>
     *   <li>Pawns</li>
     *   <li>Rooks</li>
     *   <li>Knights</li>
     *   <li>Bishops</li>
     *   <li>Queens</li>
     *   <li>Kings</li>
     * </ul>
     * 
     * <p>Each piece type is created in both WHITE and BLACK colors.
     * 
     * @return Stream of chess pieces for parameterized testing
     */
    static Stream<BasePiece> pieceProvider() {
        List<BasePiece> pieces = new ArrayList<>();

        for (Colour colour : Colour.values()) {
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
