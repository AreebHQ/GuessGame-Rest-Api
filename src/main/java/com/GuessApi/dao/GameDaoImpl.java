package com.GuessApi.dao;

import com.GuessApi.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Repository
public class GameDaoImpl implements GameDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game startGame() {

        Game game = new Game();
        game.setAnswer(generateAnswer());
        game.setInProgress(true);

        return addGame(game);
    }

    private String generateAnswer()
    {
        String answer = "";
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=1; i<10; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i=0; i<4; i++) {
            answer+=String.valueOf(list.get(i));
        }
        return answer;
    }


    @Override
    public Game addGame(Game game) {

        final String sql = "INSERT INTO game(Answer,InProgress) VALUES(?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.getAnswer());
            statement.setBoolean(2, game.isInProgress());
            return statement;

        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public Game getGame(int id) {
        final String sql = "SELECT GameId, Answer, InProgress FROM game WHERE GameId = ?;";
        return jdbcTemplate.queryForObject(sql,new GameMapper(), id);
    }

    @Override
    public List<Game> getAllGames() {


        final String sql = "SELECT GameId, Answer, InProgress FROM game;";
        return jdbcTemplate.query(sql,new GameMapper());
    }

    @Override
    public Game updateGameProgress(int id) {

        final String sql = "UPDATE game SET InProgress = false WHERE GameId = ?;";
        jdbcTemplate.update(sql,id);
        Game game = getGame(id);
        return game;
    }

    @Override
    public boolean deleteGame(int id) {
        final String sql = "DELETE FROM game WHERE GameId = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static final class GameMapper implements RowMapper<Game>
    {
        @Override
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {

            Game game = new Game();
            game.setGameId(rs.getInt("GameId"));
            game.setAnswer(rs.getString("Answer"));
            game.setInProgress(rs.getBoolean("InProgress"));

            return game;
        }
    }
}
