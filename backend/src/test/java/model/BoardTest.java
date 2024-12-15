package model;

import common.Colour;
import common.InvalidMoveException;
import common.InvalidPositionException;
import common.Position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.List;

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
    void move_whiteKingSideCastling_castlingHappens() throws InvalidPositionException, InvalidMoveException {
        // Setup: Clear squares between king and rook
        boardMap.clear();
        Position kingStart = Position.get(Colour.WHITE, 7, 4); // e1
        Position kingEnd = Position.get(Colour.WHITE, 7, 6);   // g1
        Position rookStart = Position.get(Colour.WHITE, 7, 7); // h1
        Position rookEnd = Position.get(Colour.WHITE, 7, 5);   // f1

        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        boardMap.put(kingStart, king);
        boardMap.put(rookStart, rook);

        // Perform castling
        board.move(kingStart, kingEnd);
        
        // Verify positions
        assertEquals(king, boardMap.get(kingEnd));
        assertEquals(rook, boardMap.get(rookEnd));
        assertNull(boardMap.get(kingStart));
        assertNull(boardMap.get(rookStart));
    }

    @Test
    void move_whiteQueenSideCastling_castlingHappens() throws InvalidPositionException, InvalidMoveException {
        // Setup: Clear squares between king and rook
        boardMap.clear();
        Position kingStart = Position.get(Colour.WHITE, 7, 4); // e1
        Position kingEnd = Position.get(Colour.WHITE, 7, 2);   // c1
        Position rookStart = Position.get(Colour.WHITE, 7, 0); // a1
        Position rookEnd = Position.get(Colour.WHITE, 7, 3);   // d1

        BasePiece king = new King(Colour.WHITE);
        BasePiece rook = new Rook(Colour.WHITE);
        boardMap.put(kingStart, king);
        boardMap.put(rookStart, rook);

        // Perform castling
        board.move(kingStart, kingEnd);
        
        // Verify positions
        assertEquals(king, boardMap.get(kingEnd));
        assertEquals(rook, boardMap.get(rookEnd));
        assertNull(boardMap.get(kingStart));
        assertNull(boardMap.get(rookStart));
    }

    @Test
    void getPossibleMoves_emptySquare_emptyMovesList() throws InvalidPositionException {
        Position pos = Position.get(Colour.WHITE, 4, 4); // e4
        Set<Position> possibleMoves = board.getPossibleMoves(pos);
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    void getPossibleMoves_blockedPawn_emptyMovesList() throws InvalidPositionException {
        Position pos = Position.get(Colour.WHITE, 6, 4); // e2
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

    @Test
    void move_capturePiece_targetPieceRemovedFromBoard() throws InvalidMoveException, InvalidPositionException {
        // Setup: Place a white pawn and a black pawn in capture position
        boardMap.clear();
        Position whitePawnPos = Position.get(Colour.WHITE, 4, 4); // e4
        Position blackPawnPos = Position.get(Colour.BLACK, 3, 5); // f5
        BasePiece whitePawn = new Pawn(Colour.WHITE);
        BasePiece blackPawn = new Pawn(Colour.BLACK);
        boardMap.put(whitePawnPos, whitePawn);
        boardMap.put(blackPawnPos, blackPawn);

        // Move the white pawn to capture the black pawn
        board.move(whitePawnPos, blackPawnPos);

        // Assert that the black pawn is no longer on the board
        assertNull(boardMap.get(whitePawnPos));
        assertEquals(whitePawn, boardMap.get(blackPawnPos));
        // Assert that the black pawn is in the eliminated pieces list
        Map<String, List<String>> eliminatedPieces = board.getEliminatedPieces();
        assertTrue(eliminatedPieces.get("black").contains("BP"));
    }
}
