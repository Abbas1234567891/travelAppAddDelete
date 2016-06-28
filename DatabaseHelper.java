package com.tcs.sqlitedatabaseexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 1256088 on 6/22/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper databaseHelper;

    public static final String DATABASE_NAME = "testing";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataTable.CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(ContentValues cv) {
        try {
            long id = getWritableDatabase().insert(DataTable.TABLE_NAME, null, cv);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int changePass(String mobile, String pass) {
        try {
            Cursor cursor = getWritableDatabase().query(DataTable.TABLE_NAME, new String[]{DataTable.PASSWORD}, DataTable.MOBILE + "=?", new String[]{mobile}, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                String temp = cursor.getString(0);
                if (temp.equals(pass)) {
                    return 0;
                } else {
                    return 1;
                }

            }

            if (cursor == null) {
                return 1;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 5;
}




    public int updateRecord(String mobile, String newPass) {
        ContentValues cv = new ContentValues();
        cv.put(DataTable.MOBILE, mobile);
        cv.put(DataTable.PASSWORD, newPass);
        return getWritableDatabase().update(DataTable.TABLE_NAME, cv, DataTable.MOBILE + "=?", new String[]{mobile});
    }

    public Integer deleteData(String mobile){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataTable.TABLE_NAME, "MOBILE= ?", new String[]{mobile});

    }
    public Cursor verification() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + "table_name", null);
            return c;
        }catch (SQLiteException e){
            return null;
        }
    }
}
