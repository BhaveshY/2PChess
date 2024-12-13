package common;

/**
 *  Class to throw a custom exception for invalid moves
 **/
public class InvalidMoveException extends Exception{
    public InvalidMoveException(String msg){
        super("Invalid Move: "+msg);
    }
}
