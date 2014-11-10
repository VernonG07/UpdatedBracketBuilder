package com.example.mgriffin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mgriffin.pojos.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 11/3/2014.
 */
public class GameDataSource {

    private SQLiteDatabase database;
    private DBHelper mDbHelper;

    public GameDataSource(Context context) {
        mDbHelper = new DBHelper(context, DBHelper.DBType.GAME, 1);
    }

    public void open() throws SQLException {
        database = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Game createGame (String gameName) {
        Game game = new Game();
        game.setGameName(gameName);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_GAME_NAME, gameName);

        long insertId = database.insert(DBHelper.TABLE_GAME, null, contentValues);

        game.setGameId(insertId);

        return game;
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<Game>();

        Cursor cursor = database.query(DBHelper.TABLE_GAME, new String[] {DBHelper.COLUMN_ID, DBHelper.COLUMN_GAME_NAME}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game game = cursorToGame(cursor);
            games.add(game);
            cursor.moveToNext();
        }
        cursor.close();
        return games;
    }

    public Game getExistingGameById(long id) {
        Game game = null;

        Cursor cursor = database.query(DBHelper.TABLE_GAME, new String[] {DBHelper.COLUMN_ID, DBHelper.COLUMN_GAME_NAME}, DBHelper.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        game = cursorToGame(cursor);

        return game;
    }

    public void deleteGame (Game game) {
        long id = game.getGameId();
        database.delete(DBHelper.TABLE_GAME, DBHelper.COLUMN_ID + " = " + game.getGameId(), null);
    }

    private Game cursorToGame(Cursor cursor) {
        Game game = new Game();
        game.setGameId(cursor.getLong(0));
        game.setGameName(cursor.getString(1));
        return game;
    }

    public Game editGameName (Game game, String newGameName) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_GAME_NAME, newGameName);

        database.update(DBHelper.TABLE_GAME, values, DBHelper.COLUMN_ID + " = " + game.getGameId(), null);
        game.setGameName(newGameName);

        return game;
    }
}
