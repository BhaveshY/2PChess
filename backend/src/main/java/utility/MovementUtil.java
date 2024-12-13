package utility;

import common.Direction;
import common.InvalidPositionException;
import common.Position;
import model.BasePiece;
import model.Pawn;

/**
 * MovementUtil - helper class for the movement of chess pieces
 * To validate the step with each move in different directions
 **/
public class MovementUtil {

    private static final String TAG = "MovementUtil";

    /**
     * step method to get the next position based on the direction input
     * @param piece piece to be moved
     * @param step directions to move
     * @param current current position of the piece
     * @return Position of the piece after the step
     **/
    public static Position step(BasePiece piece, Direction[] step, Position current) throws InvalidPositionException {
        boolean reverse = false;
        for(Direction d: step){
            if((piece.getColour()!=current.getColour() && piece instanceof Pawn) || reverse){//reverse directions for knights
                switch(d){
                    case FORWARD: d = Direction.BACKWARD; break;
                    case BACKWARD: d = Direction.FORWARD; break;
                    case LEFT: d = Direction.RIGHT; break;
                    case RIGHT: d = Direction.LEFT; break;
                }
            }
            Position next = current.neighbour(d);
            if(next.getColour()!= current.getColour()){//need to reverse directions when switching between sections of the board
                reverse=true;
            }
            current = next;
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
        for(Direction d: step){
            if((piece.getColour()!=current.getColour() && piece instanceof Pawn) || reverse){//reverse directions for knights
                switch(d){
                    case FORWARD: d = Direction.BACKWARD; break;
                    case BACKWARD: d = Direction.FORWARD; break;
                    case LEFT: d = Direction.RIGHT; break;
                    case RIGHT: d = Direction.LEFT; break;
                }
            }
            Position next = current.neighbour(d);
            if(next.getColour()!= current.getColour()){//need to reverse directions when switching between sections of the board
                reverse=true;
            }
            current = next;
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
            Log.e(TAG, "Exception: "+e.getMessage());
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
            Log.e(TAG, "Exception: "+e.getMessage());
            return null;
        }
    }
}
