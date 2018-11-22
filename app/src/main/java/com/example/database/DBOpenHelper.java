package com.example.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * reference
 * https://high-programmer.com/2017/08/02/android-studio-database-sqlite/
 */


public class DBOpenHelper extends SQLiteOpenHelper {

    static final private String TAG = "DbOpenHelper";
    // データベース自体の名前（テーブル名ではない）
    static final private String DATABASE_NAME = "AppData.db";
    // データベースのバージョン（2，3と影ていくとopnUpgradeメソッドが実行される）
    static final private int VERSION = 1;


    private static final String SQL_TABLE_STORIES
            = "CREATE TABLE " + Table.STORIES.getName()
            + " (" + Stories.ID.getName() + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Stories.DATE.getName() + " TEXT NOT NULL, "
            + Stories.NAME.getName() + " TEXT NOT NULL, "
            + Stories.FILE_URL.getName() + " TEXT)";

    private static final String SQL_TABLE_STORY
            = "CREATE TABLE " + Table.STORY.getName()
            + " (" + Story.ID.getName() + " INTEGER PRIMARY KEY NOT NULL, "
            + Story.TURN.getName() + " INTEGER NOT NULL, "
            + Story.NAME.getName() + " TEXT NOT NULL, "
            + Story.COMPOSITION_ID.getName() + " INTEGER NOT NULL, "
            + Story.DESCRIPTION.getName() + " TEXT NOT NULL, "
            + Story.FILE_URL.getName() + " TEXT, "
            + Story.STORIES_ID.getName() + " INTEGER NOT NULL)";

    private static final String SQL_TABLE_COMPOSITION
            = "CREATE TABLE " + Table.COMPOSITION.getName()
            + " (" + Composition.ID.getName() + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Composition.NAME.getName() + " TEXT NOT NULL, "
            + Composition.DESCRIPTION.getName() + " TEXT NOT NULL, "
            + Composition.FILE_ID.getName() + " INTEGER NOT NULL, "
            + Composition.THUMB_ID.getName() + " INTEGER NOT NULL, "
            + Composition.TAG.getName() + " TEXT NOT NULL)";
    /*
    // 生成するデータベースのテーブル
    private static final String SQL_TABLE_STORIES
            = "CREATE TABLE stories (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "date TEXT NOT NULL, " + "name TEXT NOT NULL)";

    private static final String SQL_TABLE_STORY
            = "CREATE TABLE story (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "name TEXT NOT NULL, " + "compostionID INTEGER NOT NULL, "
            + "description TEXT NOT NULL, " + "fileURL TEXT, "
            + "storesID INTEGER NOT NULL)";

    private static final String SQL_TABLE_COMPOSITION
            = "CREATE TABLE composition (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "name TEXT NOT NULL, " + "description TEXT NOT NULL, "
            + "fileID INTEGER NOT NULL)";
    */

    /*
    static final private String[] tableName  = new String [] { "stories", "story", "composition"};
    static final private String[] storiesField = new String [] { "id" ,"date" ,"name" };
    static final private String[] storyField
            = new String [] { "id" ,"name" ,"compostionID" , "description", "fileURL", "storiesID" };
    static final private String[] compositionField
            = new String [] { "id", "name", "description", "fileID"};
    */


    public DBOpenHelper(Context context){

        super(context, DATABASE_NAME, null, VERSION);
    }

    // データベースが生成されたときに実行される処理
    @Override
    public void onCreate(SQLiteDatabase db){
        // write processing

        db.execSQL(SQL_TABLE_STORIES);
        db.execSQL(SQL_TABLE_STORY);
        db.execSQL(SQL_TABLE_COMPOSITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        for(Table t : Table.values()){
            db.execSQL(t.getName());
        }
        onCreate(db);

    }

    // データベースが開かれたときに実行される処理
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }



}
