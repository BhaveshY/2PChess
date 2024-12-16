package main;

import helper.*;
import entity.Board;
import entity.BasePiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.GameInterfaceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameInterfaceImplTest {

    @Mock
    private Board board;

    @InjectMocks
    private GameInterfaceImpl gameInterfaceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onClick_selectEmptySquare_noHighlight() {
        when(board.getPiece(any(Position.class))).thenReturn(null);
        GameState response = gameInterfaceImpl.onClick("c3");
        assertEquals(0, response.getHighlightSquares().size());
    }


    @Test
    void onClick_selectNonTurnBlackPawn_noHighlight() {
        BasePiece piece = mock(BasePiece.class);
        when(piece.getColour()).thenReturn(Colour.BLACK);
        when(board.getPiece(any(Position.class))).thenReturn(piece);

        GameState response = gameInterfaceImpl.onClick("a7");
        assertEquals(0, response.getHighlightSquares().size());
    }

    @Test
    void onClick_moveWhitePawn_noHighlight() {
        BasePiece piece = mock(BasePiece.class);
        when(piece.getColour()).thenReturn(Colour.WHITE);
        when(board.getPiece(any(Position.class))).thenReturn(piece);

        gameInterfaceImpl.onClick("b2");
        GameState response = gameInterfaceImpl.onClick("b4");
        assertEquals(0, response.getHighlightSquares().size());
    }


    @Test
    void onClick_invalidMove_noHighlight() throws InvalidPositionException, InvalidMoveException {
        BasePiece piece = mock(BasePiece.class);
        when(piece.getColour()).thenReturn(Colour.WHITE);
        when(board.getPiece(any(Position.class))).thenReturn(piece);
        doThrow(new InvalidPositionException("Invalid position")).when(board).move(any(Position.class), any(Position.class));

        gameInterfaceImpl.onClick("b2");
        GameState response = gameInterfaceImpl.onClick("b5");
        assertEquals(0, response.getHighlightSquares().size());
    }



}