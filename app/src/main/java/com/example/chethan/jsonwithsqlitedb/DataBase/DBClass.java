package com.example.chethan.jsonwithsqlitedb.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBClass extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "db1";

    public static String TABLE_NAME = "tb1";

    public static String NAME = "name";
    public static String ID = "id";
    public static String YEAR = "year";
    public static String COLOR = "color";
    public static String VALUE = "value";

    public DBClass(Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME +
                "(" +
                NAME + " TEXT, " +
                ID + " TEXT, " +
                YEAR + " TEXT, " +
                COLOR + " TEXT, " +
                VALUE + " TEXT " +
                 ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }
}
