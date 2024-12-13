package model;

import common.Colour;
import common.InvalidMoveException;
import common.InvalidPositionException;
import common.Position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static common.Position.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the Board class.
 */
class BoardTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    @Test
    void move_pieceMoveToEmptySquare_startPositionEmptyAndEndPositionOccupied() throws InvalidPositionException, InvalidMoveException {
        board.move(E2R, E4R);
        assertNull(boardMap.get(E2R));
        assertNotNull(boardMap.get(E4R));
        assertTrue(boardMap.get(E4R) instanceof Pawn);
    }

    @Test
    void move_pawnToOppositeEndRow_pawnUpgradeToQueen() throws InvalidPositionException, InvalidMoveException {
        // Setup: Place a pawn near promotion square
        BasePiece redPawn = new Pawn(Colour.WHITE);
        boardMap.put(A7R, redPawn);
        boardMap.remove(A8R); // Clear the promotion square
        
        // Move pawn to promotion square
        board.move(A7R, A8R);

        // Verify promotion
        BasePiece promotedPiece = boardMap.get(A8R);
        assertInstanceOf(Queen.class, promotedPiece);
        assertEquals(Colour.WHITE, promotedPiece.getColour());
    }

    @Test
    void move_shortCastlingLegalMove_castlingHappens() throws InvalidPositionException, InvalidMoveException {
        // Setup: Clear squares between king and rook
        boardMap.remove(F1R);
        boardMap.remove(G1R);

        BasePiece king = boardMap.get(E1R);
        BasePiece rightRook = boardMap.get(H1R);

        // Perform castling
        board.move(E1R, G1R);
        
        // Verify positions
        assertEquals(king, boardMap.get(G1R));
        assertEquals(rightRook, boardMap.get(F1R));
        assertNull(boardMap.get(E1R));
        assertNull(boardMap.get(H1R));
    }

    @Test
    void move_longCastlingLegalMove_castlingHappens() throws InvalidPositionException, InvalidMoveException {
        // Setup: Clear squares between king and rook
        boardMap.remove(D1R);
        boardMap.remove(C1R);
        boardMap.remove(B1R);

        BasePiece king = boardMap.get(E1R);
        BasePiece leftRook = boardMap.get(A1R);

        // Perform castling
        board.move(E1R, C1R);
        
        // Verify positions
        assertEquals(king, boardMap.get(C1R));
        assertEquals(leftRook, boardMap.get(D1R));
        assertNull(boardMap.get(E1R));
        assertNull(boardMap.get(A1R));
    }

    @Test
    void getPossibleMoves_emptySquare_emptyMovesList() {
        Set<Position> possibleMoves = board.getPossibleMoves(E4R);
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    void getPossibleMoves_blockedPawn_emptyMovesList() {
        // A pawn blocked by another piece should have no moves
        Set<Position> possibleMoves = board.getPossibleMoves(E2R);
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    void getPossibleMoves_unobstructedPawn_hasMoves() {
        // Clear the square in front of the pawn
        boardMap.remove(E3R);
        Set<Position> possibleMoves = board.getPossibleMoves(E2R);
        assertFalse(possibleMoves.isEmpty());
    }
}
