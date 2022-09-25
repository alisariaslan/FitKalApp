package com.pakachu.fitkal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

public class Database extends SQLiteOpenHelper {

    private static final String TABLE1 = "food_list";
    private final String TABLE1_COL1 = "id";
    private final String TABLE1_COL2 = "name";
    private final String TABLE1_COL3 = "img";
    private final String TABLE1_COL4 = "gram";
    private final String TABLE1_COL5 = "protein";
    private final String TABLE1_COL6 = "carb";
    private final String TABLE1_COL7 = "fat";
    private final String TABLE1_COL8 = "tags";

    private static final String TABLE2 = "eaten_list";
    private final String TABLE2_COL1 = "id";
    private final String TABLE2_COL2 = "eaten_id";
    private final String TABLE2_COL3 = "eaten_gram";

    private static final String TABLE3 = "profile";
    private final String TABLE3_COL1 = "id";
    private final String TABLE3_COL2 = "name_surname";
    private final String TABLE3_COL3 = "age";
    private final String TABLE3_COL4 = "weight";
    private final String TABLE3_COL5 = "height";
    private final String TABLE3_COL6 = "choosen_type";

    public Database(Context context) {
        super(context, TABLE1, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE1_COL2 + " TEXT," +
                TABLE1_COL3 + " BLOB," +
                TABLE1_COL4 + " INTEGER," +
                TABLE1_COL5 + " FLOAT," +
                TABLE1_COL6 + " FLOAT," +
                TABLE1_COL7 + " FLOAT," +
                TABLE1_COL8 + " TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE2_COL2 + " INTEGER," +
                TABLE2_COL3 + " INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE3_COL2 + " TEXT," +
                TABLE3_COL3 + " INTEGER," +
                TABLE3_COL4 + " FLOAT," +
                TABLE3_COL5 + " INTEGER," +
                TABLE3_COL6 + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean add_data_to_food_list(String name, byte[] img, Integer gram, Float protein, Float carb, Float fat,String tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL2, name);
        contentValues.put(TABLE1_COL3, img);
        contentValues.put(TABLE1_COL4, gram);
        contentValues.put(TABLE1_COL5, protein);
        contentValues.put(TABLE1_COL6, carb);
        contentValues.put(TABLE1_COL7, fat);
        contentValues.put(TABLE1_COL8, tags);
        long result = db.insert(TABLE1, null, contentValues);
        return result != -1;
    }

    public boolean add_data_to_eaten_list(int eaten_id, int eaten_gram) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COL2, eaten_id);
        contentValues.put(TABLE2_COL3, eaten_gram);
        long result = db.insert(TABLE2, null, contentValues);
        return result != -1;
    }

    public boolean create_new_profile(String name, int age, float weight, int height, int choosen_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE3_COL2, name);
        contentValues.put(TABLE3_COL3, age);
        contentValues.put(TABLE3_COL4, weight);
        contentValues.put(TABLE3_COL5, height);
        contentValues.put(TABLE3_COL6, choosen_type);
        long result = db.insert(TABLE2, null, contentValues);
        return result != -1;
    }

    public Cursor get_data_with_sql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    public void execute_sql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

}