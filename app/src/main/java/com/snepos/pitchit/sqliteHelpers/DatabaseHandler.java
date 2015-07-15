package com.snepos.pitchit.sqliteHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.snepos.pitchit.Card;
import com.snepos.pitchit.database.IdeaData;

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
    private static final String KEY_PUBLISHER = "publisher";
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
        if(true)
            return;
        String CREATE_NEW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NEW_POSTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_HEAD + " TEXT," +
                KEY_PUBLISHER + " TEXT" +
                KEY_BODY + " TEXT" +
                KEY_isLiked + " INTEGER" +
                KEY_isOnIt + " INTEGER" +
                KEY_isReport + " INTEGER" +
                KEY_upVotes + " INTEGER" +
                KEY_onItVotes + " INTEGER" +")";
        String CREATE_GROWING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROWING_POSTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_HEAD + " TEXT," +
                KEY_PUBLISHER + " TEXT" +
                KEY_BODY + " TEXT" +
                KEY_isLiked + " INTEGER" +
                KEY_isOnIt + " INTEGER" +
                KEY_isReport + " INTEGER" +
                KEY_upVotes + " INTEGER" +
                KEY_onItVotes + " INTEGER" +")";
        String CREATE_POPULAR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_POPULAR_POSTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_HEAD + " TEXT," +
                KEY_PUBLISHER + " TEXT" +
                KEY_BODY + " TEXT" +
                KEY_isLiked + " INTEGER" +
                KEY_isOnIt + " INTEGER" +
                KEY_isReport + " INTEGER" +
                KEY_upVotes + " INTEGER" +
                KEY_onItVotes + " INTEGER" +")";

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

    private String getTableKey(Table table)
    {
        switch (table) {
            case NEW:
                return TABLE_NEW_POSTS;
            case GROWING:
                return TABLE_GROWING_POSTS;
            case POPULAR:
                return TABLE_POPULAR_POSTS;
        }
        return "NULL";
    }

    public void refreshTable(Table table, Card[] cards)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableKey = getTableKey(table);

        db.execSQL("DROP TABLE IF EXISTS " + tableKey);

        System.out.println("Refresh table: " + tableKey);

        String CREATE_NEW_TABLE = "CREATE TABLE IF NOT EXISTS " + tableKey + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_HEAD + " TEXT," +
                KEY_PUBLISHER + " TEXT, " +
                KEY_BODY + " TEXT, " +
                KEY_isLiked + " INTEGER, " +
                KEY_isOnIt + " INTEGER, " +
                KEY_isReport + " INTEGER, " +
                KEY_upVotes + " INTEGER, " +
                KEY_onItVotes + " INTEGER" +")";

        db.execSQL(CREATE_NEW_TABLE);

        for(int i = 0; i < cards.length; i++)
            addPost(cards[i], tableKey);

        db.close(); // Closing database connection
    }

    private void addPost(Card card, String tableKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, card.getId());
        values.put(KEY_HEAD, card.getHead());
        values.put(KEY_PUBLISHER, card.getPublisherName());
        values.put(KEY_BODY, card.getBody());
        values.put(KEY_isLiked, card.getIsLiked() ? 1 : 0);
        values.put(KEY_isReport, card.getIsReport() ? 1 : 0);
        values.put(KEY_isOnIt, card.getIsOnIt() ? 1 : 0);
        values.put(KEY_upVotes, card.getUpVotes());
        values.put(KEY_onItVotes, card.getOnItVotes());

        db.insert(tableKey, null, values);

        db.close(); // Closing database connection
    }

    public Card getPost(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEW_POSTS, new String[] { KEY_ID,
                        KEY_HEAD, KEY_BODY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Card post = new Card(Integer.parseInt(cursor.getString(0)), "",cursor.getString(1), cursor.getString(2),false,0,false,false,0,0);
        // return contact
        return post;
    }

    public Card[] getAllPosts(Table table) {
        List<Card> cardsList = new ArrayList<Card>();
        // Select All Query

        String tableKey = getTableKey(table);

        String selectQuery = "SELECT  * FROM " + tableKey;

        System.out.println("Loading table: " + tableKey);

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            System.out.println("Starting");

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    System.out.println("another card");
                    Card card = new Card();
                    card.setCard(Integer.parseInt(
                            cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4) == 1,
                            0,
                            cursor.getInt(5) == 1,
                            cursor.getInt(6) == 1,
                            cursor.getInt(7),
                            cursor.getInt(8));
                    cardsList.add(card);
                } while (cursor.moveToNext());
            }

            System.out.println("Done");

            // return contact list
            return cardsList.toArray(new Card[cardsList.size()]);
        }
        catch (Exception e)
        {
            System.out.println("Failed");
            System.out.println(e.getMessage());
            return new Card[0];
        }
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
