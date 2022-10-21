package com.example.friendsforevergroupchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "user_loging";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UID = "uid";
    public DBHelper(Context context) // create the Database
    {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        db.execSQL("create table user_loging" + "(id integer primary key,uid integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user_loging");
        onCreate(db);
    }
    public boolean insertContact (String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", uid);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
        public boolean deleteContact()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("user_loging", "" ,null);
//            db.execSQL("delete from" + TABLE_NAME);
            return true;
        }
    public Cursor getYourTableContents()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
