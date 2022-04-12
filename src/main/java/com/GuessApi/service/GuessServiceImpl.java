package com.GuessApi.service;

import com.GuessApi.dao.GameDao;
import com.GuessApi.dao.RoundDao;
import com.GuessApi.models.Game;
import com.GuessApi.models.Round;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class GuessServiceImpl implements GuessService{

    GameDao gameDao;
    RoundDao roundDao;
    GuessServiceImpl(GameDao gameDao,RoundDao roundDao)
    {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public Game startGame() {
        return gameDao.startGame();
    }



    private void processGuess(Round round, Game game)
    {
        int exactCounter=0;
        int partialCounter=0;

            for(int i=0; i<4; i++)
            {
                if(game.getAnswer().contains(String.valueOf(round.getGuess().charAt(i))))
                {
                    partialCounter++;
                    if(game.getAnswer().charAt(i) == round.getGuess().charAt(i))
                    {
                        exactCounter++;
                    }
                }
            }

        if(exactCounter==4)
        {
            gameDao.updateGameProgress(game.getGameId());
        }
        round.setResult("e"+exactCounter+":p"+partialCounter);

    }


    @Override
    public Round checkResult(Round round)
    {
        Game game = gameDao.getGame(round.getGameId());
        round.setResult(game.getAnswer());
        processGuess(round,game);

        return roundDao.addRound(round,game);
    }

    @Override
    public Game getGame(int id) {
        return gameDao.getGame(id);
    }

    @Override
    public List<Game> getAllGames() {

        List<Game> allGames = gameDao.getAllGames();
        for(Game game : allGames)
        {
            //if game is in progress, don't show the answer.
            if(game.isInProgress())
            {
                game.setAnswer("");
            }
        }

        return allGames;
    }


    @Override
    public List<Round> getAllRounds(int id) {

        return roundDao.getAllRounds(id);
    }
}
