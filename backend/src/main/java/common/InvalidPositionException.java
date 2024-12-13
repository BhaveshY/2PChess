package common;

/**
 *  Class to throw a custom exception for invalid positions
 **/
public class InvalidPositionException extends Exception{
    public InvalidPositionException(String msg){
        super("Invalid Position: "+msg);
    }
}
