package entity;

import helper.Colour;
import helper.InvalidMoveException;
import helper.InvalidPositionException;
import helper.Position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.getBoardMap();
    }

    @Test
    void move_pieceMoveToEmptySquare_startPositionEmptyAndEndPositionOccupied() throws InvalidPositionException, InvalidMoveException {
        Position start = Position.get(Colour.WHITE, 6, 4); // e2
        Position end = Position.get(Colour.WHITE, 4, 4);   // e4
        board.move(start, end);
        assertNull(boardMap.get(start));
        assertNotNull(boardMap.get(end));
        assertTrue(boardMap.get(end) instanceof Pawn);
    }

    @Test
    void move_whitePawnToLastRow_pawnUpgradeToQueen() throws InvalidPositionException, InvalidMoveException {
        // Setup: Place a white pawn near promotion square
        boardMap.clear();
        Position start = Position.get(Colour.WHITE, 1, 0); // a7
        Position end = Position.get(Colour.WHITE, 0, 0);   // a8
        BasePiece whitePawn = new Pawn(Colour.WHITE);
        boardMap.put(start, whitePawn);
        
        // Move pawn to promotion square
        board.move(start, end);

        // Verify promotion
        BasePiece promotedPiece = boardMap.get(end);
        assertInstanceOf(Queen.class, promotedPiece);
        assertEquals(Colour.WHITE, promotedPiece.getColour());
    }


    @Test
    void getPossibleMoves_emptySquare_emptyMovesList() throws InvalidPositionException {
        Position pos = Position.get(Colour.WHITE, 4, 4); // e4
        Set<Position> possibleMoves = board.getPossibleMoves(pos);
        assertTrue(possibleMoves.isEmpty());
    }


    @Test
    void getPossibleMoves_unobstructedPawn_hasMoves() throws InvalidPositionException {
        Position pawnPos = Position.get(Colour.WHITE, 6, 4); // e2
        Position frontPos = Position.get(Colour.WHITE, 5, 4); // e3
        boardMap.remove(frontPos);
        Set<Position> possibleMoves = board.getPossibleMoves(pawnPos);
        assertFalse(possibleMoves.isEmpty());
    }

}
