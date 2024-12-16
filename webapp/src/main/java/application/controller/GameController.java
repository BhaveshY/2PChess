package application.controller;

import service.IGameInterface;
import helper.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final IGameInterface game;

    @Autowired
    public GameController(IGameInterface game) {
        this.game = game;
    }

    @GetMapping("/newGame")
    public void newGame() {
        // Additional logic can be added here if necessary
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
