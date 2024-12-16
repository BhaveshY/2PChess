package helper;

/**
 * Represents a position on the three-player chess board.
 * 
 * <p>This class implements a dual color-space coordinate system where each physical square
 * can be referenced from both WHITE and BLACK color spaces. This allows for:
 * <ul>
 *   <li>Relative piece movement calculations</li>
 *   <li>Color-specific board perspectives</li>
 *   <li>Complex move validation</li>
 *   <li>Piece interaction between color spaces</li>
 * </ul>
 * 
 * <p>Positions are immutable and cached in a static pool to ensure consistency
 * and memory efficiency.
 * 
 * @see Colour
 * @see Direction
 * @version 1.0
 */
public enum Position {
  // WHITE section positions (bottom)
  A1R(Colour.WHITE, 7, 0), A2R(Colour.WHITE, 6, 0), A3R(Colour.WHITE, 5, 0), A4R(Colour.WHITE, 4, 0),
  A5R(Colour.WHITE, 3, 0), A6R(Colour.WHITE, 2, 0), A7R(Colour.WHITE, 1, 0), A8R(Colour.WHITE, 0, 0),
  B1R(Colour.WHITE, 7, 1), B2R(Colour.WHITE, 6, 1), B3R(Colour.WHITE, 5, 1), B4R(Colour.WHITE, 4, 1),
  B5R(Colour.WHITE, 3, 1), B6R(Colour.WHITE, 2, 1), B7R(Colour.WHITE, 1, 1), B8R(Colour.WHITE, 0, 1),
  C1R(Colour.WHITE, 7, 2), C2R(Colour.WHITE, 6, 2), C3R(Colour.WHITE, 5, 2), C4R(Colour.WHITE, 4, 2),
  C5R(Colour.WHITE, 3, 2), C6R(Colour.WHITE, 2, 2), C7R(Colour.WHITE, 1, 2), C8R(Colour.WHITE, 0, 2),
  D1R(Colour.WHITE, 7, 3), D2R(Colour.WHITE, 6, 3), D3R(Colour.WHITE, 5, 3), D4R(Colour.WHITE, 4, 3),
  D5R(Colour.WHITE, 3, 3), D6R(Colour.WHITE, 2, 3), D7R(Colour.WHITE, 1, 3), D8R(Colour.WHITE, 0, 3),
  E1R(Colour.WHITE, 7, 4), E2R(Colour.WHITE, 6, 4), E3R(Colour.WHITE, 5, 4), E4R(Colour.WHITE, 4, 4),
  E5R(Colour.WHITE, 3, 4), E6R(Colour.WHITE, 2, 4), E7R(Colour.WHITE, 1, 4), E8R(Colour.WHITE, 0, 4),
  F1R(Colour.WHITE, 7, 5), F2R(Colour.WHITE, 6, 5), F3R(Colour.WHITE, 5, 5), F4R(Colour.WHITE, 4, 5),
  F5R(Colour.WHITE, 3, 5), F6R(Colour.WHITE, 2, 5), F7R(Colour.WHITE, 1, 5), F8R(Colour.WHITE, 0, 5),
  G1R(Colour.WHITE, 7, 6), G2R(Colour.WHITE, 6, 6), G3R(Colour.WHITE, 5, 6), G4R(Colour.WHITE, 4, 6),
  G5R(Colour.WHITE, 3, 6), G6R(Colour.WHITE, 2, 6), G7R(Colour.WHITE, 1, 6), G8R(Colour.WHITE, 0, 6),
  H1R(Colour.WHITE, 7, 7), H2R(Colour.WHITE, 6, 7), H3R(Colour.WHITE, 5, 7), H4R(Colour.WHITE, 4, 7),
  H5R(Colour.WHITE, 3, 7), H6R(Colour.WHITE, 2, 7), H7R(Colour.WHITE, 1, 7), H8R(Colour.WHITE, 0, 7),

  // BLACK section positions (top)
  A8B(Colour.BLACK, 0, 0), A7B(Colour.BLACK, 1, 0), A6B(Colour.BLACK, 2, 0), A5B(Colour.BLACK, 3, 0),
  A4B(Colour.BLACK, 4, 0), A3B(Colour.BLACK, 5, 0), A2B(Colour.BLACK, 6, 0), A1B(Colour.BLACK, 7, 0),
  B8B(Colour.BLACK, 0, 1), B7B(Colour.BLACK, 1, 1), B6B(Colour.BLACK, 2, 1), B5B(Colour.BLACK, 3, 1),
  B4B(Colour.BLACK, 4, 1), B3B(Colour.BLACK, 5, 1), B2B(Colour.BLACK, 6, 1), B1B(Colour.BLACK, 7, 1),
  C8B(Colour.BLACK, 0, 2), C7B(Colour.BLACK, 1, 2), C6B(Colour.BLACK, 2, 2), C5B(Colour.BLACK, 3, 2),
  C4B(Colour.BLACK, 4, 2), C3B(Colour.BLACK, 5, 2), C2B(Colour.BLACK, 6, 2), C1B(Colour.BLACK, 7, 2),
  D8B(Colour.BLACK, 0, 3), D7B(Colour.BLACK, 1, 3), D6B(Colour.BLACK, 2, 3), D5B(Colour.BLACK, 3, 3),
  D4B(Colour.BLACK, 4, 3), D3B(Colour.BLACK, 5, 3), D2B(Colour.BLACK, 6, 3), D1B(Colour.BLACK, 7, 3),
  E8B(Colour.BLACK, 0, 4), E7B(Colour.BLACK, 1, 4), E6B(Colour.BLACK, 2, 4), E5B(Colour.BLACK, 3, 4),
  E4B(Colour.BLACK, 4, 4), E3B(Colour.BLACK, 5, 4), E2B(Colour.BLACK, 6, 4), E1B(Colour.BLACK, 7, 4),
  F8B(Colour.BLACK, 0, 5), F7B(Colour.BLACK, 1, 5), F6B(Colour.BLACK, 2, 5), F5B(Colour.BLACK, 3, 5),
  F4B(Colour.BLACK, 4, 5), F3B(Colour.BLACK, 5, 5), F2B(Colour.BLACK, 6, 5), F1B(Colour.BLACK, 7, 5),
  G8B(Colour.BLACK, 0, 6), G7B(Colour.BLACK, 1, 6), G6B(Colour.BLACK, 2, 6), G5B(Colour.BLACK, 3, 6),
  G4B(Colour.BLACK, 4, 6), G3B(Colour.BLACK, 5, 6), G2B(Colour.BLACK, 6, 6), G1B(Colour.BLACK, 7, 6),
  H8B(Colour.BLACK, 0, 7), H7B(Colour.BLACK, 1, 7), H6B(Colour.BLACK, 2, 7), H5B(Colour.BLACK, 3, 7),
  H4B(Colour.BLACK, 4, 7), H3B(Colour.BLACK, 5, 7), H2B(Colour.BLACK, 6, 7), H1B(Colour.BLACK, 7, 7);

  private final Colour colour;
  private final int row;
  private final int column;

  /**
   * Creates a new position with specified color space and coordinates.
   * 
   * <p>This constructor is private to enforce the use of the static factory
   * method {@link #get(Colour, int, int)} which manages the position pool.
   * 
   * @param colour Color space of the position
   * @param row Row coordinate (0-7)
   * @param column Column coordinate (0-7)
   */
  Position(Colour colour, int row, int column) {
    this.colour = colour;
    this.row = row;
    this.column = column;
  }

  /**
   * Gets the color space of this position.
   * 
   * @return Color space (WHITE or BLACK)
   */
  public Colour getColour() {
    return colour;
  }

  /**
   * Gets the row coordinate.
   * 
   * @return Row number (0-7)
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the column coordinate.
   * 
   * @return Column number (0-7)
   */
  public int getColumn() {
    return column;
  }

  /**
   * Converts position to standard chess notation.
   * 
   * <p>For example:
   * <ul>
   *   <li>(0,0) -> "a8"</li>
   *   <li>(4,4) -> "e4"</li>
   *   <li>(7,7) -> "h1"</li>
   * </ul>
   * 
   * @return Position in algebraic chess notation
   */
  @Override
  public String toString() {
    return String.format("%c%d", getColumnChar(column), 8 - row);
  }

  /**
   * Gets a position from the position pool or creates a new one.
   * 
   * <p>This method ensures that only one Position object exists for each
   * unique combination of color space and coordinates.
   * 
   * @param colour Color space of the position
   * @param row Row coordinate (0-7)
   * @param column Column coordinate (0-7)
   * @return Position object from the pool
   * @throws InvalidPositionException if coordinates are invalid
   */
  public static Position get(Colour colour, int row, int column) throws InvalidPositionException {
    for (Position position : Position.values()) {
      if (position.colour == colour && position.row == row && position.column == column) {
        return position;
      }
    }
    throw new InvalidPositionException(
        String.format("No such position: colour=%s, row=%d, column=%d", 
        colour, row, column));
  }

  /**
   * Converts column number to chess notation letter.
   * 
   * @param column Column number (0-7)
   * @return Chess notation letter (a-h)
   */
  private char getColumnChar(int column) {
    switch (column) {
      case 0: return 'a';
      case 1: return 'b';
      case 2: return 'c';
      case 3: return 'd';
      case 4: return 'e';
      case 5: return 'f';
      case 6: return 'g';
      case 7: return 'h';
      default: return '\0';
    }
  }

  /**
   * Gets the neighboring position in the specified direction.
   * 
   * <p>This method handles:
   * <ul>
   *   <li>Color-specific movement (WHITE moves up, BLACK moves down)</li>
   *   <li>Board boundaries</li>
   *   <li>Direction translation</li>
   * </ul>
   * 
   * @param direction Movement direction
   * @return Position after moving in the specified direction
   * @throws InvalidPositionException if the move would go off the board
   */
  public Position neighbour(Direction direction) throws InvalidPositionException {
    int newRow = this.row;
    int newColumn = this.column;

    switch (direction) {
      case FORWARD:
        newRow = (this.colour == Colour.WHITE) ? newRow - 1 : newRow + 1;
        break;
      case BACKWARD:
        newRow = (this.colour == Colour.WHITE) ? newRow + 1 : newRow - 1;
        break;
      case LEFT:
        newColumn--;
        break;
      case RIGHT:
        newColumn++;
        break;
    }

    return Position.get(this.colour, newRow, newColumn);
  }
}