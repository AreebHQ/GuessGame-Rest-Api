package com.GuessApi.dao;

import com.GuessApi.TestApplicationConfiguration;
import com.GuessApi.models.Game;
import com.GuessApi.models.Round;
import com.GuessApi.service.GuessService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoImplTest extends TestCase {

    @Autowired
    GameDao gameDao;
    @Autowired
    RoundDao roundDao;
    @Autowired
    GuessService service;

    @Before
    public void setUp()
    {
        List<Game> games = gameDao.getAllGames();
        if(!games.isEmpty()) {
            for (Game game : games) {
                List<Round> rounds = roundDao.getAllRounds(game.getGameId());
                for (Round round : rounds) {
                    roundDao.deleteRound(round.getRoundId());
                }
                gameDao.deleteGame(game.getGameId());
            }
        }

    }

    @Test
    public void testAddGetGame()
    {
        Game game = new Game();
        game.setGameId(1);
        game.setAnswer("6789");
        game.setInProgress(true);
        gameDao.addGame(game);

        Game fromDao = gameDao.getGame(game.getGameId());
        assertEquals(game.getGameId(),fromDao.getGameId());
        assertEquals(game.isInProgress(),fromDao.isInProgress());
    }

    @Test
    public void testAddGetAllGames()
    {
        Game game = new Game();
        game.setGameId(1);
        game.setAnswer("1234");
        game.setInProgress(true);
        gameDao.addGame(game);

        Game game2 = new Game();
        game2.setGameId(2);
        game2.setAnswer("5678");
        game2.setInProgress(true);
        gameDao.addGame(game2);

        List<Game> games = gameDao.getAllGames();
        assertEquals(2,games.size());
        assertEquals(games.get(0).getGameId(),game.getGameId());
        assertEquals(games.get(1).getGameId(),game2.getGameId());
    }

    @Test
    public void testAddGetRound()
    {
        Game game = new Game();
        game.setGameId(5);
        game.setAnswer("1234");
        game.setInProgress(true);
        gameDao.addGame(game);

        Round newRound = new Round();
        newRound.setGameId(game.getGameId());
        newRound.setGuess("1234");
        Round getRound = service.checkResult(newRound);

        assertEquals(getRound.getGameId(),newRound.getGameId());
        assertEquals(getRound.getResult(),"e4:p4");
        assertEquals(getRound.getGuess(),"1234");
    }

    @Test
    public void testAddGetAllRounds()
    {
        Game game = new Game();
        //its auto increment, need to confirm game id before testing
        game.setGameId(10);
        game.setAnswer("1234");
        game.setInProgress(true);
        gameDao.addGame(game);

        Round newRound = new Round();
        newRound.setGameId(game.getGameId());
        newRound.setGuess("1234");
        Round getRound = service.checkResult(newRound);

        Round newRound2 = new Round();
        newRound2.setGameId(game.getGameId());
        newRound2.setGuess("4567");
        Round getRound2 = service.checkResult(newRound2);

        List<Round> allRounds = roundDao.getAllRounds(10);

        assertEquals(allRounds.size(),2);
        assertEquals(getRound.getGameId(),newRound.getGameId());
        assertEquals(getRound.getResult(),"e4:p4");
        assertEquals(getRound.getGuess(),"1234");
        assertEquals(getRound2.getGameId(),newRound2.getGameId());
        assertEquals(getRound2.getResult(),"e0:p1");
        assertEquals(getRound2.getGuess(),"4567");
    }
}