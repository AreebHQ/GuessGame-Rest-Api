package com.GuessApi.controller;

import com.GuessApi.models.Game;
import com.GuessApi.models.Round;
import com.GuessApi.service.GuessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/game")
public class controller {
    private final GuessService service;

    public controller(GuessService service)
    {
        this.service = service;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int startGame()
    {
        Game game = new Game();
        game = service.startGame();
        return game.getGameId();
    }


    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.OK)
    public Round startRound(@RequestBody Round round )
    {
        Round newRound = new Round();
        newRound.setGameId(round.getGameId());
        newRound.setGuess(round.getGuess());

        return service.checkResult(newRound);
    }
    @GetMapping("/games")
    public List<Game> getAllGames()
    {
        return service.getAllGames();
    }

    @GetMapping("/game/{id}")
    public Game getGame(@PathVariable int id)
    {
        return service.getGame(id);
    }

    @GetMapping("/rounds/{id}")
    public List<Round> getAllRounds(@PathVariable int id)
    {
        return service.getAllRounds(id);
    }



}
