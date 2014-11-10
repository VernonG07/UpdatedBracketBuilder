package com.example.mgriffin.pojos;

/**
 * Created by mgriffin on 10/27/2014.
 */
public class Team {

    private String teamName;
    private long teamId;

    private boolean is_winner = false;

    public Team () {};

    public Team (String teamName) {
        this.teamName = teamName;
        this.is_winner = false;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamName (String teamName) {
        this.teamName = teamName;
    }
    public String getTeamName () {
        return teamName;
    }
}
