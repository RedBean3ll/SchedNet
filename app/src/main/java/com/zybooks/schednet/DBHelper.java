package com.zybooks.schednet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";


    public DBHelper( Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(UserID primary key autoincrement not null, email TEXT, name TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("drop table if exists users");

    }

    public Boolean insertData(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("password", password);
        values.put("email", email);

        //values.put("password", password);

        long result = db.insert("users", null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }}

        public Boolean checkEmail (String email){
        SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from users where email=?", new String[]{email});
            if (cursor.getCount() > 0) {
                return true;

            } else {
                return false;
            }
        }

        public Boolean checkpassword (String email, String password){
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from users where email =? and password =?", new String[]{email, password});
            if(cursor.getCount()>0){
                return true;
            }
            else{
                return false;
            }
        }


    }
