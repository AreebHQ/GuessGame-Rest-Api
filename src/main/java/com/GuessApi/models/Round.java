package com.GuessApi.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Round {

    private int roundId;
    private String result;
    private String guess;
    private int gameId;
    private Timestamp roundTime;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Timestamp getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(Timestamp roundTime) {
        this.roundTime = roundTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return roundId == round.roundId && gameId == round.gameId && Objects.equals(result, round.result) && Objects.equals(guess, round.guess) && Objects.equals(roundTime, round.roundTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundId, result, guess, gameId, roundTime);
    }
}
