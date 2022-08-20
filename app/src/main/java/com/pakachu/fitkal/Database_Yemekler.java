package com.pakachu.fitkal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Yemekler extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "yemeksepeti";
    private static final String COL1 = "id";
    private static final String COL2 = "imgid";
    private static final String COL3 = "ad";
    private static final String COL4 = "ayrinti";
    private static final String COL5 = "porsiyon";
    private static final String COL6 = "gram";
    private static final String COL7 = "protein";
    private static final String COL8 = "karb";
    private static final String COL9 = "yag";
    private static final String COL10 = "sivi";
    private static final String COL11 = "tagler";

    public Database_Yemekler(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " INTEGER," +
                COL3 + " TEXT," +
                COL4 + " TEXT," +
                COL5 + " TEXT," +
                COL6 + " INTEGER," +
                COL7 + " REAL," +
                COL8 + " REAL," +
                COL9 + " REAL," +
                COL10 + " INTEGER," +
                COL11 + " TEXT)";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void executeSQL(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public boolean addData(Integer imgid, String ad, String ayrinti,String porsiyon, Integer gram, Double protein, Double karb, Double yag, Integer sivi, String tagler) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, imgid);
        contentValues.put(COL3, ad);
        contentValues.put(COL4, ayrinti);
        contentValues.put(COL5, porsiyon);
        contentValues.put(COL6, gram);
        contentValues.put(COL7, protein);
        contentValues.put(COL8, karb);
        contentValues.put(COL9, yag);
        contentValues.put(COL10, sivi);
        contentValues.put(COL11, tagler);
        long result = db.insert(TABLE_NAME, null, contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public Boolean deleteFromIndex(Integer index) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL1 + " = ?", new String[]{index.toString()}) > 0;
    }

    //
//    /**
//     * Returns only the ID that matches the name passed in
//     * @param name
//     * @return
//     */
//    public Cursor getItemID(String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
//                " WHERE " + COL2 + " = '" + name + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }
//
//    /**
//     * Updates the name field
//     * @param newName
//     * @param id
//     * @param oldName
//     */
//    public void updateName(String newName, int id, String oldName){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
//                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
//                " AND " + COL2 + " = '" + oldName + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
//        db.execSQL(query);
//    }
//
//    /**
//     * Delete from database
//     * @param id
//     * @param name
//     */




}