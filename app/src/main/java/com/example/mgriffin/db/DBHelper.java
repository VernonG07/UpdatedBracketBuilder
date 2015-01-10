package com.example.mgriffin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Matt on 11/3/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";

    //Game DB
    public static final String TABLE_GAME = "game";
    public static final String COLUMN_GAME_NAME = "gameName";
    public static final String COLUMN_GAME_WINNER = "gameWinner";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_GAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_GAME_NAME
            + " text not null, " + COLUMN_GAME_WINNER + " text);";

    //MatchUp Table
    public static final String TABLE_MATCH_UP = "match_up";
    public static final String COLUMN_TEAM_ONE_ID = "teamOneId";
    public static final String COLUMN_TEAM_TWO_ID = "teamTwoId";
    public static final String COLUMN_GAME_ID = "gameId";
    public static final String COLUMN_WINNER_ID = "winnerId";
    public static final String COLUMN_ROUND_NUMBER = "roundNumber";
    public static final String COLUMN_TEAM_ONE_NAME = "teamOneName";
    public static final String COLUMN_TEAM_TWO_NAME = "teamTwoName";
    public static final String COLUMN_WINNER_NAME = "winnerName";
    public static final String COLUMN_WINS_REQUIRED = "winsRequired";
    public static final String COLUMN_WINS_TEAM_ONE = "winsTeamOne";
    public static final String COLUMN_WINS_TEAM_TWO = "winsTeamTwo";

    private static final String DATABASE_CREATE_MATCHUP = "create table "
            + TABLE_MATCH_UP + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TEAM_ONE_ID
            + " integer, " + COLUMN_TEAM_TWO_ID
            + " integer, " + COLUMN_GAME_ID
            + " integer, " + COLUMN_WINNER_ID
            + " integer, " + COLUMN_ROUND_NUMBER
            + " integer, " + COLUMN_TEAM_ONE_NAME
            + " integer, " + COLUMN_TEAM_TWO_NAME
            + " integer, " + COLUMN_WINNER_NAME + " integer);";

    //Team Table
    public static final String TABLE_TEAM = "team";
    public static final String COLUMN_TEAM_NAME = "teamName";

    private static final String DATABASE_CREATE_TEAM = "create table "
            + TABLE_TEAM + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TEAM_NAME
            + " text not null);";

    public enum DBType {
        GAME ("game.db"),
        MATCH_UP ("matchUp.db"),
        TEAM ("team.db");

        String dbName;

        private DBType (String dbName) {
            this.dbName = dbName;
        }

        @Override
        public String toString() {
            return dbName;
        }
    }

    private DBType dbType;
    public DBHelper (Context context, DBType dbType, int databaseVersion) {
        super(context, dbType.dbName, null, databaseVersion);
        this.dbType = dbType;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String dbName = null;

        switch (dbType) {
            case GAME: dbName = DATABASE_CREATE;
                break;
            case TEAM: dbName = DATABASE_CREATE_TEAM;
                break;
            case MATCH_UP: dbName = DATABASE_CREATE_MATCHUP;
                break;
        }
        if (dbName != null)
            sqLiteDatabase.execSQL(dbName);


        switch (dbType) {
            case TEAM:

                ContentValues values = new ContentValues();
                values.put(DBHelper.COLUMN_ID, -1);
                values.put(DBHelper.COLUMN_TEAM_NAME, "BYE");

                sqLiteDatabase.insert(DBHelper.TABLE_TEAM, null, values);
            break;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        onCreate(sqLiteDatabase);
    }
}
