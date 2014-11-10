package com.example.mgriffin.pojos;

/**
 * Created by Matt on 11/5/2014.
 */
public class MatchUp{

    private long id;
    private long teamOneId;
    private String teamOneName;
    private long teamTwoId;
    private String teamTwoName;
    private long gameId;
    private String winnerName;
    private long winnerId;

    //setters
    public void setId(long id) {
        this.id = id;
    }
    public void setTeamOneId(long teamOneId) {
        this.teamOneId = teamOneId;
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public void setTeamTwoId(long teamTwoId) {
        this.teamTwoId = teamTwoId;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    //getters
    public long getId() {
        return id;
    }
    public long getTeamOneId() {
        return teamOneId;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public long getTeamTwoId() {
        return teamTwoId;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }

    public long getGameId() {
        return gameId;
    }

    public void setWinnerId(long winnerId) {
        this.winnerId = winnerId;
    }

    public long getWinnerId() {
        return winnerId;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }
}