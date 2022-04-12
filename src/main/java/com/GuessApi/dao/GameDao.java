package com.GuessApi.dao;

import com.GuessApi.models.Game;

import java.util.List;

public interface GameDao {
    Game startGame();
    Game addGame(Game game);
    Game getGame(int id);
    List<Game> getAllGames();
    Game updateGameProgress(int id);
    boolean deleteGame(int id);
}
