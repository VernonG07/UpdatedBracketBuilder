package com.example.mgriffin.pojos;

/**
 * Created by Matt on 11/3/2014.
 */
public class Game {

    private String gameName;
    private long gameId;
    private String winner;

    public void setWinner (String winner) {
        this.winner = winner;
    }
    public String getWinner() {
        return winner;
    }
    public void setGameName (String name) {
        this.gameName = name;
    }
    public String getGameName () {
        return gameName;
    }
    public void setGameId (long id) {
        this.gameId = id;
    }
    public long getGameId () {
        return gameId;
    }

    @Override
    public String toString() {

        return gameName;
    }
}
