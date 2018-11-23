package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.example.camera.R;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper implements Serializable {

    private int id = 0;

    public static final String TAG = "DBHelper";
    public static final String URL = "new";

    public SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    public DBHelper(final Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDB();
    }

    public void readDB(){
        if(this.dbOpenHelper != null){
            this.db = this.dbOpenHelper.getReadableDatabase();
        }
    }

    public void writeDB(){
        if(this.dbOpenHelper != null){
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    private void establishDB(){
        if(this.db == null){
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup(){
        if(this.db != null){
            this.db.close();
            this.db = null;
        }
    }

    /**
     * Databaseが削除できればtrue。できなければfalse
     * @param context
     * @return
     */
    public boolean isDatabaseDelete(final Context context) {
        boolean result = false;
        if (this.db != null) {
            File file = context.getDatabasePath(dbOpenHelper.getDatabaseName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result = this.db.deleteDatabase(file);
            }
        }
        return result;
    }

    public void setComposition(){
        List<String[]> datas = new ArrayList<>();
        String str [] = new String []{
                "id", "三分割法",
                "交点に被写体を配置するとバランスよく撮影することができます",
                String.valueOf(R.mipmap.lattice),
                String.valueOf(R.mipmap.lattice_small),
                Tag.OTHER.getName()
        };
        datas.add(str);
        str = new String [] {
                "id", "人物配置（右）",
                "人が移動する方向や人の目線方向（左）に空間を持たせることで，"
                        + "水平方向に動きを持たせます",
                String.valueOf(R.mipmap.right_person),
                String.valueOf(R.mipmap.right_person_small),
                Tag.PORTRAIT.getName()
        };
        datas.add(str);
        str = new String [] {
                "id", "人物配置（左）",
                "人が移動する方向や人の目線方向（右）に空間を持たせることで，"
                        + "水平方向に動きを持たせます",
                String.valueOf(R.mipmap.left_person),
                String.valueOf(R.mipmap.left_person_small),
                Tag.PORTRAIT.getName()
        };
        datas.add(str);

        for(int i = 0; i < datas.size(); i++){
            setRecord(Table.COMPOSITION, datas.get(i));
        }
    }

    public boolean setRecord(Table table, String[] str){

        writeDB();
        db.beginTransaction();

        final ContentValues values = new ContentValues(1);
        switch(table){
            case STORIES:
                values.clear();
                if(str[Stories.ID.getNumber()] != "id"){
                    values.put(Stories.ID.getName(), str[Stories.ID.getNumber()]);
                }
                values.put(Stories.DATE.getName(), str[Stories.DATE.getNumber()]);
                values.put(Stories.NAME.getName(), str[Stories.NAME.getNumber()]);
                values.put(Stories.FILE_URL.getName(), str[Stories.FILE_URL.getNumber()]);
                break;
            case STORY:
                values.clear();
                if(str[Story.ID.getNumber()] != "id"){
                    values.put(Story.ID.getName(), str[Story.ID.getNumber()]);
                }
                values.put(Story.TURN.getName(), Integer.parseInt(str[Story.TURN.getNumber()]));
                values.put(Story.NAME.getName(), str[Story.NAME.getNumber()]);
                values.put(Story.COMPOSITION_ID.getName(),
                        Integer.parseInt(str[Story.COMPOSITION_ID.getNumber()]));
                values.put(Story.DESCRIPTION.getName(), str[Story.DESCRIPTION.getNumber()]);
                values.put(Story.FILE_URL.getName(), str[Story.FILE_URL.getNumber()]);
                values.put(Story.STORIES_ID.getName(),
                        Integer.parseInt(str[Story.STORIES_ID.getNumber()]));
                break;
            case COMPOSITION:
                values.clear();
                if(str[Composition.ID.getNumber()] != "id"){
                    values.put(Composition.ID.getName(), str[Composition.ID.getNumber()]);
                }
                values.put(Composition.NAME.getName(), str[Composition.NAME.getNumber()]);
                values.put(Composition.DESCRIPTION.getName(), str[Composition.DESCRIPTION.getNumber()]);
                values.put(Composition.FILE_ID.getName(), str[Composition.FILE_ID.getNumber()]);
                values.put(Composition.THUMB_ID.getName(),
                        Integer.parseInt(str[Composition.THUMB_ID.getNumber()]));
                values.put(Composition.TAG.getName(), str[Composition.TAG.getNumber()]);
                break;
        }
        db.insert(table.getName(), null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    /**
     * 任意のテーブルに新しいレコードをセットする
     * number : テーブルの番号
     * str    : セットする文字列
     * return :
     */
    /*
    public boolean setRecord(int number, String str){

        DBTable[] Enum = DBTable.values();
        final DBTable num = Enum[number];


        writeDB();
        db.beginTransaction();

        final ContentValues values = new ContentValues(1);
        switch(num){
            case stories:
                String command = "select count(*) from " + dbOpenHelper.getTableName()[number];
                readDB();
                Cursor c = db.rawQuery(command, null);
                c.moveToFirst();

                if(c == null){
                    return false;
                }
                id = c.getInt(0);
                c.close();

                values.clear();
                values.put("id", id);
                values.put("name", str);
                values.put("date", getNowDateTime());
                db.insert("stories", null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
                break;
            case story:
                break;
            case composition:
                break;
        }
        db.close();
        return true;
    }
    */

    /**
     * 任意のテーブルのフィールドに値を挿入
     * table : 列挙型テーブル
     * primaryKey : プライマリーキー
     * column : 挿入したい値のカラムの番号
     * value : 挿入したい値
     */
    public void setField(Table table, String primaryKey, String column, String value){

        String command
                = "update " + table.getName() + " set " + column
                + " = \"" + value + "\" where id = " + primaryKey + ";";
        writeDB();
        db.execSQL(command);
        db.close();

    }

    /**
     * 任意の名前の絵コンテ（作品）が存在するかを返す
     * return : true = 存在する　false = 存在しない
     *
     */
    public boolean existStoryName(String name){
        String command = "select count(*) from stories where name = ?" ;

        readDB();
        Cursor c = db.rawQuery(command, new String[] {name});

        c.moveToFirst();


        db.close();

        if(c == null){
            return false;
        }

        int result = c.getInt(0);
        c.close();

        if(result == 0){
            return false;
        }
        return true;

    }

    /**
     * レコードの数を返す
     * number : テーブルの番号
     * return : レコードの数
     *
     */
    public int getNumOfRecord(Table table){

        readDB();
        int count = 0;
        String command = "select count(*) from " + table.getName();
        readDB();
        Cursor c = db.rawQuery(command, null);
        c.moveToFirst();

        if(c == null){
            return -1;
        }
        count = c.getInt(0);
        db.close();
        c.close();
        return count;
    }

    /**
     * 条件を満たすレコードの数を返す
     * number : テーブルの番号
     * conditionColumn : 条件となる部分の列の名前
     * condition : 一致してほしい文字列
     * return : レコードの数
     *
     */
    public int getNumOfRecord(Table table, String conditionColumn, String condition){

        readDB();
        int count = 0;
        String command
                = "select count(*) from " + table.getName()
                + " where " + conditionColumn + " = \"" + condition + "\";";
        readDB();
        Cursor c = db.rawQuery(command, null);
        c.moveToFirst();

        if(c == null){
            return -1;
        }
        count = c.getInt(0);
        db.close();
        c.close();
        return count;
    }

    /**
     * テーブル内にある任意の列の値をすべて返す
     * dbNumber : テーブル番号
     * column   : 列の名前
     * return   : 列にあるすべての値
     */
    public String [] getColumn(Table table, String column){

        if(getNumOfRecord(table) == 0){
            return null;
        }

        String command = "select " + column + " from " + table.getName() + ";";

        int count = getNumOfRecord(table);

        readDB();
        Cursor c = db.rawQuery(command, null);
        if(c == null){
            return null;
        }

        String [] str = new String [count];

        boolean notEnd = c.moveToFirst();
        int i = 0;


        while(notEnd) {
            str[i] = c.getString(0);
            notEnd = c.moveToNext();
            i++;
        }
        c.moveToFirst();
        c.close();
        db.close();

        return str;
    }

    /**
     * テーブル内にある，条件（イコール）を満たした任意の列の値を返す
     * column : 返してほしい列の名前
     * conditionColumn : 条件となる部分の列の名前
     * condition : 一致してほしい文字列
     */
    public String [] getColumn(Table table, String column, String conditionColumn, String condition){

        if(getNumOfRecord(table) == 0){
            return null;
        }


        String command
                = "select " + column + " from " + table.getName()
                + " where " + conditionColumn + " = \"" + condition + "\";";

        int count = getNumOfRecord(table, conditionColumn, condition);

        readDB();
        SQLiteCursor c = (SQLiteCursor) db.rawQuery(command, null);
        if(c == null){
            return null;
        }

        String [] str = new String [count];

        boolean notEnd = c.moveToFirst();
        int i = 0;


        while(notEnd) {
            str[i] = c.getString(0);
            notEnd = c.moveToNext();
            i++;
        }
        c.moveToFirst();
        c.close();
        db.close();

        return str;
    }

    public String [] getRecord(Table table, String primaryKey){

        if(getNumOfRecord(table) == 0){
            return null;
        }


        String command
                = "select * " + " from " + table.getName()
                + " where ID = " + primaryKey + ";";

        readDB();
        SQLiteCursor c = (SQLiteCursor) db.rawQuery(command, null);
        if(c == null){
            return null;
        }

        String [] str = new String [c.getColumnCount()];
        // int i = 0;

        boolean notEnd = c.moveToFirst();

        for(int i = 0; i < c.getColumnCount(); i++) {
            str[i] = c.getString(i);
        }
        c.moveToFirst();
        c.close();
        db.close();

        return str;

    }

    public void deleteRecord(Table table, String primaryKey){
        String command
                = "delete from " + table.getName()
                + " where ID = " + primaryKey + ";";
        writeDB();
        db.execSQL(command);
        db.close();
    }

    public void deleteStoryRecord(String primaryKey, String storiesID){

        String iDs[]
                = getColumn(
                Table.STORY, Story.ID.getName(),
                Story.STORIES_ID.getName(), String.valueOf(storiesID));

        String orders[]
                = getColumn(
                Table.STORY, Story.TURN.getName(),
                Story.STORIES_ID.getName(), String.valueOf(storiesID));

        // プライマリーキーが格納されている配列番号
        int pointPrimary = 0;

        for(int i = 0; i < iDs.length; i++){
            if(iDs[i].equals(primaryKey)){
                pointPrimary = i;
            }
        }

        writeDB();
        for(int i = 0; i < iDs.length; i++) {
            if(i == pointPrimary){
                continue;
            }
            if(Integer.parseInt(orders[pointPrimary]) < Integer.parseInt(orders[i])) {
                int order = Integer.parseInt(orders[i]) - 1;
                setField(Table.STORY, iDs[i], Story.TURN.getName(), String.valueOf(order));
            }
        }

        deleteRecord(Table.STORY, primaryKey);
        db.close();
    }

    /**
     * 現在の日付を返す
     * return : 現在の日付
     */
    public static String getNowDateTime(){
        final DateFormat df = new SimpleDateFormat("yyMMdd_HHmmss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

}
