package helper;

/**
 * Enum class containing the colors of different players.
 * Also has a method for String representation to use in web app.
 */
public enum Colour {

    WHITE, BLACK;

    public Colour next() {
        return this == WHITE ? BLACK : WHITE;
    }

    @Override
    public String toString() {
        return this == WHITE ? "W" : "B";
    }
}

