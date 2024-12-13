package common;

/**
 *  Enum class containing the Directions for the piece movement
 *  Also has a method for String representation to use in web app
 **/
public enum Direction{
    FORWARD,BACKWARD,LEFT,RIGHT;

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
