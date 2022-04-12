package com.GuessApi.dao;

import com.GuessApi.models.Game;
import com.GuessApi.models.Round;

import java.util.List;

public interface RoundDao {

    Round addRound(Round round, Game game);
    List<Round> getAllRounds(int id);
    boolean deleteRound(int id);
}
