package utility;

import helper.Direction;
import helper.InvalidPositionException;
import helper.Position;
import model.BasePiece;

/**
 * MovementUtil - helper class for the movement of chess pieces
 * To validate the step with each move in different directions
 **/
public class MovementUtil {

    /**
     * step method to get the next position based on the direction input
     * @param piece piece to be moved
     * @param step directions to move
     * @param current current position of the piece
     * @return Position of the piece after the step
     **/
    public static Position step(BasePiece piece, Direction[] step, Position current) throws InvalidPositionException {
        for(Direction d: step) {
            current = current.neighbour(d);
        }
        return current;
    }

    /**
     * step method to get the next position based on the direction input
     * @param piece piece to be moved
     * @param step directions to move
     * @param current current position of the piece
     * @param reverse if movement is in reverse direction
     * @return Position of the piece after the step
     **/
    public static Position step(BasePiece piece, Direction[] step, Position current, boolean reverse) throws InvalidPositionException {
        for(Direction d: step) {
            current = current.neighbour(d);
        }
        return current;
    }


    /**
     * step method to get the next position based on the direction input, return null if not valid
     * @param piece piece to be moved
     * @param step directions to move
     * @param current current position of the piece
     * @return Position of the piece after the step
     **/
    public static Position stepOrNull(BasePiece piece, Direction[] step, Position current) {
        try {
            return step(piece, step, current);
        } catch (InvalidPositionException e) {
            return null;
        }
    }

    /**
     * step method to get the next position based on the direction input, return null if not valid
     * @param piece piece to be moved
     * @param step directions to move
     * @param current current position of the piece
     * @param reverse if movement is in reverse direction
     * @return Position of the piece after the step
     **/
    public static Position stepOrNull(BasePiece piece, Direction[] step, Position current, boolean reverse) {
        try {
            return step(piece, step, current, reverse);
        } catch (InvalidPositionException e) {
            return null;
        }
    }
}
