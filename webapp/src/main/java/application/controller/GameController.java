package application.controller;

import abstraction.IGameInterface;
import common.GameState;
import main.GameMain;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GameController {

    private IGameInterface game;

    public GameController() {
        game = new GameMain();
    }

    @GetMapping("/newGame")
    public void newGame() {
        game = new GameMain();
    }

    @GetMapping("/board")
    @ResponseBody
    public GameState getBoard() {
        return game.getBoard();
    }

    @PostMapping("/onClick")
    @ResponseBody
    public GameState onClick(@RequestBody String polygonText) {
        return game.onClick(polygonText);
    }

    @GetMapping("/currentPlayer")
    @ResponseBody
    public String getCurrentPlayer() {
        return game.getTurn().toString();
    }

    @GetMapping("/boardState")
    @ResponseBody
    public GameState getBoardState() {
        return game.getBoard();
    }
}