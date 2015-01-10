package com.example.mgriffin.db;

/**
 * Created by Matt on 11/5/2014.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.pojos.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchUpDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TEAM_ONE_ID,
            DBHelper.COLUMN_TEAM_TWO_ID,
            DBHelper.COLUMN_GAME_ID,
            DBHelper.COLUMN_WINNER_ID,
            DBHelper.COLUMN_ROUND_NUMBER,
            DBHelper.COLUMN_TEAM_ONE_NAME,
            DBHelper.COLUMN_TEAM_TWO_NAME,
            DBHelper.COLUMN_WINNER_NAME};

    private Context context;

    public MatchUpDataSource(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, DBHelper.DBType.MATCH_UP, 6);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //There is no winner at this point
    public MatchUp createMatchUp (long teamOneId, long teamTwoId, long gameId, int roundNumber, String teamOneName, String teamTwoName) {
        MatchUp matchUp = new MatchUp();
        matchUp.setTeamOneId(teamOneId);
        matchUp.setTeamTwoId(teamTwoId);
        matchUp.setTeamOneName(teamOneName);
        matchUp.setTeamTwoName(teamTwoName);


        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TEAM_ONE_ID, teamOneId);
        values.put(DBHelper.COLUMN_TEAM_TWO_ID, teamTwoId);
        values.put(DBHelper.COLUMN_GAME_ID, gameId);
        values.put(DBHelper.COLUMN_ROUND_NUMBER, roundNumber);
        values.put(DBHelper.COLUMN_TEAM_ONE_NAME, teamOneName);
        values.put(DBHelper.COLUMN_TEAM_TWO_NAME, teamTwoName);

        long insertId = database.insert(DBHelper.TABLE_MATCH_UP, null, values);
        matchUp.setId(insertId);

        return matchUp;
    }

    public MatchUp assignMatchUpWinner( long matchUpId, long winnerId, String winnerName) {
        MatchUp matchUp = new MatchUp();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_WINNER_ID, winnerId);
        values.put(DBHelper.COLUMN_WINNER_NAME, winnerName);

        database.update(DBHelper.TABLE_MATCH_UP, values, DBHelper.COLUMN_ID + " = " + matchUpId, null);
        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, allColumns, DBHelper.COLUMN_ID + " = " + matchUpId, null, null, null, null);
        cursor.moveToFirst();

        matchUp.setId(cursor.getLong(0)); //MatchUpId
        matchUp.setTeamOneId(cursor.getLong(1)); //TeamOneId
        matchUp.setTeamTwoId(cursor.getLong(2)); //TeamTwoId
        matchUp.setGameId(cursor.getLong(3)); //GameId
        matchUp.setWinnerId(cursor.getLong(4));
        matchUp.setWinnerName(cursor.getString(8));

        return matchUp;
    }

    public List<MatchUp> getAllMatchUps(long gameId, int roundNumber) {
        List<MatchUp> matchUps = new ArrayList<MatchUp>();

        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, allColumns, DBHelper.COLUMN_GAME_ID + " = " + gameId + " and " + DBHelper.COLUMN_ROUND_NUMBER + " = " + roundNumber, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MatchUp matchUp = cursorToMatchUp(cursor);
            matchUps.add(matchUp);
            cursor.moveToNext();
        }

        return matchUps;
    }

    public MatchUp getMatchUpById (long matchUpId) {
        MatchUp matchUp = null;

        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, allColumns, DBHelper.COLUMN_ID + " = " + matchUpId, null, null, null, null);
        cursor.moveToFirst();

        matchUp = cursorToMatchUp(cursor);

        return matchUp;
    }

    private MatchUp cursorToMatchUp (Cursor cursor) {

        MatchUp matchUp = new MatchUp();

        matchUp.setId(cursor.getLong(0));
        matchUp.setTeamOneId(cursor.getLong(1));
        matchUp.setTeamTwoId(cursor.getLong(2));
        matchUp.setGameId(cursor.getLong(3));
        matchUp.setTeamOneName(cursor.getString(6));
        matchUp.setTeamTwoName(cursor.getString(7));
        matchUp.setWinnerId(cursor.getLong(4));
        matchUp.setWinnerName(cursor.getString(8));

        return matchUp;
    }

    public void deleteMatchUp (MatchUp matchUp) {
        long id = matchUp.getId();
        database.delete(DBHelper.TABLE_MATCH_UP, DBHelper.COLUMN_ID + " = " + matchUp.getId(), null);
    }

    public MatchUp editMatchUp (MatchUp matchUp, Team teamOne, Team teamTwo) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TEAM_ONE_NAME, teamOne.getTeamName());
        values.put(DBHelper.COLUMN_TEAM_ONE_ID, teamOne.getTeamId());
        values.put(DBHelper.COLUMN_TEAM_TWO_NAME, teamTwo.getTeamName());
        values.put(DBHelper.COLUMN_TEAM_TWO_ID, teamTwo.getTeamId());

        database.update(DBHelper.TABLE_MATCH_UP, values, DBHelper.COLUMN_ID + " = " + matchUp.getId(), null);
        matchUp.setTeamOneName(teamOne.getTeamName());
        matchUp.setTeamTwoName(teamTwo.getTeamName());

        return matchUp;
    }

    public boolean isRoundTwoStarted (long gameId) {
        boolean isStarted = false;

        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, new String[] {DBHelper.COLUMN_ROUND_NUMBER}, DBHelper.COLUMN_GAME_ID + " = " + gameId + " and " + DBHelper.COLUMN_ROUND_NUMBER + " = 2", null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount()!=0)
            isStarted = true;

        return isStarted;
    }

    public boolean isNextRoundStarted (long gameId, int roundNumber) {
        boolean isStarted = false;

        roundNumber = roundNumber + 1;

        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, new String[] {DBHelper.COLUMN_ROUND_NUMBER}, DBHelper.COLUMN_GAME_ID + " = " + gameId + " and " + DBHelper.COLUMN_ROUND_NUMBER + " = " + roundNumber, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            isStarted = false;
        } else {
            isStarted = true;
        }

        cursor.close();

        return  isStarted;
    }

    public List<MatchUp> getMatchupsByGameId(long gameId){
        //TODO

        List<MatchUp> matchUps = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_MATCH_UP, allColumns, DBHelper.COLUMN_GAME_ID + " = " + gameId, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MatchUp matchUp = cursorToMatchUp(cursor);
            matchUps.add(matchUp);
            cursor.moveToNext();
        }
        return  matchUps;
    }
}

