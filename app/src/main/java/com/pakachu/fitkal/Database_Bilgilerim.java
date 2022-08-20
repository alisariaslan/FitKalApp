package com.pakachu.fitkal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Bilgilerim extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "profil";
    private static final String COL1 = "id";
    private static final String COL2 = "ad";
    private static final String COL3 = "yas";
    private static final String COL4 = "boy";
    private static final String COL5 = "kilo";
    private static final String COL6 = "cinsiyet";
    private static final String COL7 = "aktiviteduzeyicarpani";
    private static final String COL8 = "adim";
    private static final String COL9 = "kalori";
    private static final String COL10 = "spor";

    public Database_Bilgilerim(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," +
                COL3 + " INTEGER," +
                COL4 + " REAL," +
                COL5 + " REAL," +
                COL6 + " INTEGER," +
                COL7 + " REAL," +
                COL8 + " INTEGER," +
                COL9 + " INTEGER," +
                COL10 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String ad, Integer yas, Double boy, Double kilo, Integer cinsiyet, Double aktiviteduzeyicarpani, Integer adim, Integer kalori, Integer spor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, ad);
        contentValues.put(COL3, yas);
        contentValues.put(COL4, boy);
        contentValues.put(COL5, kilo);
        contentValues.put(COL6, cinsiyet);
        contentValues.put(COL7, aktiviteduzeyicarpani);
        contentValues.put(COL8, adim);
        contentValues.put(COL9, kalori);
        contentValues.put(COL10, spor);
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
    public void deleteName() {
        SQLiteDatabase db = this.getWritableDatabase();

//        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
//                + COL1 + " = '" + id + "'" +
//                " AND " + COL2 + " = '" + name + "'";

//                String query = "DELETE FROM " + TABLE_NAME + " WHERE "
//                + COL1 + " = '" + id + "'";

        String query = "DELETE FROM " + TABLE_NAME;

//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);

    }
}
