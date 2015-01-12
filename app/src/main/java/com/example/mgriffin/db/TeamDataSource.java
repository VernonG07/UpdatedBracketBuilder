package com.example.mgriffin.db;

/**
 * Created by Matt on 11/5/2014.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mgriffin.pojos.Team;

import java.sql.SQLException;

/**
 * Created by i337994 on 11/5/2014.
 */
public class TeamDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TEAM_NAME};

    public TeamDataSource(Context context) {
        dbHelper = new DBHelper(context, DBHelper.DBType.TEAM, 4);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Team createTeam (String teamName) {
        Team team = new Team();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TEAM_NAME, teamName);
        long insertId = database.insert(DBHelper.TABLE_TEAM, null, values);

        team.setTeamId(insertId);
        team.setTeamName(teamName);

        return team;
    }

    public Team editTeamName (long teamId, String newTeamName) {
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName(newTeamName);

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TEAM_NAME, newTeamName);

        database.update(DBHelper.TABLE_TEAM, values, DBHelper.COLUMN_ID + " = " + teamId, null);

        return team;
    }

    public Team getExistingTeam (long teamId) {

        Team team = new Team();

        Cursor cursor = database.query(DBHelper.TABLE_TEAM, allColumns, DBHelper.COLUMN_ID + " = " + teamId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            team.setTeamId(cursor.getLong(0));
            team.setTeamName(cursor.getString(1));

            cursor.moveToNext();
        }

        return team;
    }

    public void deleteTeam(long teamId) {

        if (teamId != -1)
            database.delete(DBHelper.TABLE_TEAM, DBHelper.COLUMN_ID + " = " + teamId, null);
    }
}
