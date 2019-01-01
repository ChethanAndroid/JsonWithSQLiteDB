package com.example.chethan.jsonwithsqlitedb.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    DBClass dbClass;
    Context context;
    DBPojo dbPojo;
    SQLiteDatabase db;


    public DBHelper(Context context) {
        this.context = context;

        dbClass = new DBClass(context, DBClass.DATABASE_NAME, null, DBClass.DATABASE_VERSION);
        db = dbClass.getWritableDatabase();
    }

    public void ADDIntoDb(DBPojo pojo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBClass.NAME, pojo.getName());
        contentValues.put(DBClass.ID, pojo.getId());
        contentValues.put(DBClass.YEAR, pojo.getYear());
        contentValues.put(DBClass.COLOR, pojo.getColor());
        contentValues.put(DBClass.VALUE, pojo.getValue());

        try {
            db.insert(DBClass.TABLE_NAME, null, contentValues);
            System.out.println("Data Inserted Into SQLite DB");
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    public List<DBPojo> Fetch() {
        Cursor cursor = null;
        List<DBPojo> stringList = new ArrayList<>();

        try {
            db = dbClass.getReadableDatabase();
            cursor = db.query(DBClass.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                dbPojo = new DBPojo();

                dbPojo.setName(cursor.getString(cursor.getColumnIndex(DBClass.NAME)));
                dbPojo.setId(cursor.getString(cursor.getColumnIndex(DBClass.ID)));
                dbPojo.setYear(cursor.getString(cursor.getColumnIndex(DBClass.YEAR)));
                dbPojo.setColor(cursor.getString(cursor.getColumnIndex(DBClass.COLOR)));
                dbPojo.setValue(cursor.getString(cursor.getColumnIndex(DBClass.VALUE)));

                stringList.add(dbPojo);


            }
        } finally {

            if (db != null && db.isOpen()) {

                db.close();
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }


            return stringList;
        }


    }

    public void Delete(String id){

        Cursor cursor = null;



        try {
            db = dbClass.getWritableDatabase();
            cursor = db.query(DBClass.TABLE_NAME,null,null,null,null,null,null);

            while (cursor.moveToNext()){
                String where = DBClass.ID + "="+ id + "";

                db.delete(DBClass.TABLE_NAME,where,null);
            }

        } finally {

            if (!cursor.isClosed()){
                cursor.close();
            }

            if (db!=null && db.isOpen()){
                db.close();
            }
        }

    }

    public void  DeleteAll(){
        Cursor cursor = null;


        try {
            db = dbClass.getWritableDatabase();
            db.delete(DBClass.TABLE_NAME,null,null);
            System.out.println("All Data Deleted");

        } finally {

            if (db!=null && db.isOpen()){
                db.close();
            }

        }

    }



}
