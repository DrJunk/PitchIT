package com.snepos.pitchit.sqliteHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.snepos.pitchit.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 19/06/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PostsManager";
    public enum Table {
        NEW, GROWING, POPULAR
    }

    // Contacts table name
    private static final String TABLE_NEW_POSTS = "newPosts";
    private static final String TABLE_GROWING_POSTS = "growingPosts";
    private static final String TABLE_POPULAR_POSTS = "popularPosts";


    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_HEAD = "head";
    private static final String KEY_BODY = "body";
    private static final String KEY_isLiked = "isLiked";
    private static final String KEY_isOnIt = "isOnIt";
    private static final String KEY_isReport = "isReport";
    private static final String KEY_upVotes = "upVotes";
    private static final String KEY_onItVotes = "onItVotes";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NEW_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HEAD + " TEXT,"
                + KEY_BODY + " TEXT" +
                KEY_isLiked + " TEXT" +
                KEY_isOnIt + " TEXT" +
                KEY_isReport + " TEXT" +
                KEY_upVotes + " TEXT" +
                KEY_onItVotes + " TEXT" +")";
        String CREATE_GROWING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROWING_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HEAD + " TEXT,"
                + KEY_BODY + " TEXT" +
                KEY_isLiked + " TEXT" +
                KEY_isOnIt + " TEXT" +
                KEY_isReport + " TEXT" +
                KEY_upVotes + " TEXT" +
                KEY_onItVotes + " TEXT" +")";
        String CREATE_POPULAR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_POPULAR_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HEAD + " TEXT,"
                + KEY_BODY + " TEXT" +
                KEY_isLiked + " TEXT" +
                KEY_isOnIt + " TEXT" +
                KEY_isReport + " TEXT" +
                KEY_upVotes + " TEXT" +
                KEY_onItVotes + " TEXT" +")";

        db.execSQL(CREATE_NEW_TABLE);
        db.execSQL(CREATE_GROWING_TABLE);
        db.execSQL(CREATE_POPULAR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROWING_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_POSTS);

        // Create tables again
        onCreate(db);
    }
    public void addPost(Card card, Table table) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, card.getId());
        values.put(KEY_HEAD, card.getHead()); // Contact Name
        values.put(KEY_BODY, card.getBody()); // Contact Phone Number

        // Inserting Row
        switch (table) {
            case NEW:
                db.insert(TABLE_NEW_POSTS, null, values);
                break;
            case GROWING:
                db.insert(TABLE_GROWING_POSTS, null, values);
                break;
            case POPULAR:
                db.insert(TABLE_POPULAR_POSTS, null, values);
                break;
        }
        db.close(); // Closing database connection
    }

    public Card getPost(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEW_POSTS, new String[] { KEY_ID,
                        KEY_HEAD, KEY_BODY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Card post = new Card(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),false,0,false,false,0,0);
        // return contact
        return post;
    }
    public List<Card> getAllPosts(Table table) {
        List<Card> cardList = new ArrayList<Card>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " ;
        switch (table) {
            case NEW:
                selectQuery += TABLE_NEW_POSTS;
                break;
            case GROWING:
                selectQuery += TABLE_GROWING_POSTS;
                break;
            case POPULAR:
                selectQuery += TABLE_POPULAR_POSTS;
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card();
                card.setCard(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),false,0,false,false,0,0);
                cardList.add(card);
            } while (cursor.moveToNext());
        }

        // return contact list
        return cardList;
    }
    public int getPostsCount(Table table) {
        String countQuery = "SELECT  * FROM ";
        switch (table) {
            case NEW:
                countQuery += TABLE_NEW_POSTS;
                break;
            case GROWING:
                countQuery += TABLE_GROWING_POSTS;
                break;
            case POPULAR:
                countQuery += TABLE_POPULAR_POSTS;
                break;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;

    }

}
