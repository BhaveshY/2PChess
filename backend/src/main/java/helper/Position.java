package helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

/**
 * Represents a position on the chess board.
 * This class uses the Flyweight pattern to cache and reuse position instances,
 * treating them as references rather than values.
 */
public class Position {
    private static final Map<String, Position> POSITION_CACHE = new ConcurrentHashMap<>();
    
    private final Colour colour;
    private final int row;
    private final int column;
    
    /**
     * Get a position instance. Creates new one if not cached.
     * 
     * @param colour Color space of the position
     * @param row Row number (0-7)
     * @param column Column number (0-7)
     * @return Position instance
     * @throws InvalidPositionException if position is invalid
     */
    public static Position get(Colour colour, int row, int column) throws InvalidPositionException {
        validatePosition(row, column);
        String key = createKey(colour, row, column);
        return POSITION_CACHE.computeIfAbsent(key, k -> new Position(colour, row, column));
    }
    
    /**
     * Private constructor to enforce factory method usage
     */
    private Position(Colour colour, int row, int column) {
        this.colour = colour;
        this.row = row;
        this.column = column;
    }
    
    /**
     * Create a cache key for a position
     */
    private static String createKey(Colour colour, int row, int column) {
        return String.format("%s_%d_%d", colour, row, column);
    }
    
    /**
     * Validate position coordinates
     */
    private static void validatePosition(int row, int column) throws InvalidPositionException {
        if (row < 0 || row > 7 || column < 0 || column > 7) {
            throw new InvalidPositionException(
                String.format("Invalid position: row=%d, column=%d", row, column));
        }
    }
    
    /**
     * Move from current position using given directions
     */
    public Position move(Direction[] directions) throws InvalidPositionException {
        int newRow = row;
        int newColumn = column;
        
        for (Direction direction : directions) {
            switch (direction) {
                case FORWARD:
                    newRow = colour == Colour.WHITE ? newRow - 1 : newRow + 1;
                    break;
                case BACKWARD:
                    newRow = colour == Colour.WHITE ? newRow + 1 : newRow - 1;
                    break;
                case LEFT:
                    newColumn--;
                    break;
                case RIGHT:
                    newColumn++;
                    break;
            }
        }
        
        return Position.get(colour, newRow, newColumn);
    }
    
    public Colour getColour() {
        return colour;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return row == position.row &&
               column == position.column &&
               colour == position.colour;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(colour, row, column);
    }
    
    @Override
    public String toString() {
        return String.format("%s(%d,%d)", colour, row, column);
    }
    
    /**
     * Clear the position cache (mainly for testing purposes)
     */
    public static void clearCache() {
        POSITION_CACHE.clear();
    }
}