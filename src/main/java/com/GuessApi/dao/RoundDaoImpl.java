package com.GuessApi.dao;

import com.GuessApi.models.Game;
import com.GuessApi.models.Round;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RoundDaoImpl implements RoundDao{

    private final JdbcTemplate jdbcTemplate;

    public RoundDaoImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round addRound(Round round, Game game) {


        final String sql = "INSERT INTO round(GameId,Guess,Result,Roundtime) VALUES(?,?,?,?);";
        //generating timestamp
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        round.setRoundTime(time);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, round.getGameId());
            statement.setString(2, round.getGuess());
            statement.setString(3, round.getResult());
            statement.setString(4, round.getRoundTime().toString());
            return statement;

        }, keyHolder);

        round.setRoundId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<Round> getAllRounds(int id) {
        String sql = "SELECT RoundId,GameId,Guess,Result,Roundtime FROM round WHERE GameId = ? ORDER BY Roundtime DESC;";
        return jdbcTemplate.query(sql,new RoundMapper(),id);
    }

    @Override
    public boolean deleteRound(int id) {
        final String sql = "DELETE FROM round WHERE RoundId = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static final class RoundMapper implements RowMapper<Round>
    {
        @Override
        public Round mapRow(ResultSet rs, int rowNum) throws SQLException {

            Round round = new Round();
            round.setRoundId(rs.getInt("RoundId"));
            round.setGameId(rs.getInt("GameId"));
            round.setGuess(rs.getString("Guess"));
            round.setResult(rs.getString("Result"));
            round.setRoundTime(Timestamp.valueOf(rs.getString("Roundtime")));

            return round;
        }
    }
}
