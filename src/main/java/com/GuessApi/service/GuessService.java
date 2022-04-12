package com.GuessApi.service;


import com.GuessApi.models.Game;
import com.GuessApi.models.Round;

import java.util.List;

public interface GuessService {

    Game startGame();
    Game getGame(int id);
    List<Game> getAllGames();
    List<Round> getAllRounds(int id);
    Round checkResult(Round round);



}
