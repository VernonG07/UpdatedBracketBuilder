package com.example.mgriffin.listviewex;

import java.io.Serializable;

/**
 * Created by mgriffin on 10/27/2014.
 */
public class GameMatchUp implements Serializable {

    private Team teamOne;
    private Team teamTwo;
    private int id;

    public void setId (int id) {
        this.id = id;
    }
    public int getId () {
        return id;
    }
    public void setTeamOne (Team teamOne) {
        this.teamOne = teamOne;
    }
    public Team getTeamOne () {
        return teamOne;
    }
    public void setTeamTwo (Team teamTwo) {
        this.teamTwo = teamTwo;
    }
    public Team getTeamTwo () {
        return teamTwo;
    }

    public static Team getWinner(GameMatchUp gameMatchUp) {
        if (gameMatchUp.getTeamOne().getWinner())
            return gameMatchUp.getTeamOne();
        else
            return gameMatchUp.getTeamTwo();

    }

    public static boolean hasWinner (GameMatchUp gameMatchUp) {
        if (gameMatchUp.getTeamOne().getWinner() || gameMatchUp.getTeamTwo().getWinner()) {
            return true;
        } else {
            return false;
        }
    }
}
