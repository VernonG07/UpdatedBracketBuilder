package com.example.mgriffin.listviewex;

/**
 * Created by mgriffin on 10/27/2014.
 */
public class Team {

    private String teamName;

    private boolean is_winner = false;

    public Team (String teamName) {
        this.teamName = teamName;
        this.is_winner = false;
    }

    public void setTeamName (String teamName) {
        this.teamName = teamName;
    }
    public String getTeamName () {
        return teamName;
    }
    public void setWinner ( boolean is_winner) {
        this.is_winner = is_winner;
    }
    public boolean getWinner () {
        return is_winner;
    }
}
