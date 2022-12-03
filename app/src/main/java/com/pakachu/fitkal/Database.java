package com.pakachu.fitkal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

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

    private static final String TABLE3 = "profile_list";
    private final String TABLE3_COL1 = "id";
    private final String TABLE3_COL2 = "profile_name";
    private final String TABLE3_COL3 = "age";
    private final String TABLE3_COL4 = "weight";
    private final String TABLE3_COL5 = "height";
    private final String TABLE3_COL6 = "is_selected";

    private static final String TABLE4 = "tag_list";
    private final String TABLE4_COL1 = "id";
    private final String TABLE4_COL2 = "tag";

    public Database(Context context) {
        super(context, "my_database", null, 2);
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

        sql = "CREATE TABLE " + TABLE4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE4_COL2 + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean add_data_to_food_list(String name, byte[] img, Integer gram, Float protein, Float carb, Float fat, String tags) {
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

    public boolean add_data_to_profile_list(String name, int age, float weight, int height,int is_selected) {
        if(is_selected==1)
            execute_sql("UPDATE "+TABLE3+" SET is_selected=0");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE3_COL2, name);
        contentValues.put(TABLE3_COL3, age);
        contentValues.put(TABLE3_COL4, weight);
        contentValues.put(TABLE3_COL5, height);
        contentValues.put(TABLE3_COL6, is_selected);
        long result = db.insert(TABLE3, null, contentValues);
        return result != -1;
    }

    public boolean delete_data_from_profile_list(String profile_name) {
        Cursor c = get_data_with_sql("SELECT * FROM profile_list WHERE profile_name='" + profile_name + "'");
        if (c.moveToNext()) {
            int id = c.getInt(0);
            String new_query = "DELETE FROM profile_list WHERE id=" + id;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(new_query);
            return true;
        }
        return false;
    }

    public boolean update_data_for_profile_list(String profile_name, int age, float weight, int height) {
            String new_query = "UPDATE profile_list SET age = " + age + ", weight = " + weight + ", height = " + height + ", profile_name = '" + profile_name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(new_query);
            return true;
    }

    public boolean add_data_to_tag_list(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE4_COL2, tag);
        long result = db.insert(TABLE4, null, contentValues);
        return result != -1;
    }

    public int get_tag_id(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM tag_list WHERE tag='" + tag + "'", null);
        if (data.moveToNext())
            return data.getInt(0);
        else return -1;
    }

    public int get_food_id(String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM food_list WHERE name='" + food + "'", null);
        if (data.moveToNext())
            return data.getInt(0);
        else return -1;
    }

    public String get_tag_from_id(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM tag_list WHERE id='" + id + "'", null);
        if (data.moveToNext())
            return data.getString(1);
        else return null;
    }

    public Cursor get_data_with_sql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    public int check_profile() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE3, null);
        if (data.moveToNext())
            return 1;
        return 0;
    }

    public Cursor getProfile(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE3 + " WHERE profile_name='" + name + "'", null);
        return data;
    }

    public ArrayList<String> get_tags_array(String sql) {
        ArrayList<String> tags = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(sql, null);
        while (data.moveToNext()) {
            tags.add(data.getString(1));
        }
        return tags;
    }

    public void execute_sql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

}