package com.jinwang.jianwutong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chenss on 2015/11/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private final String CREATE_BOOK_CHAT = "create table tbChat(" +
            "id integer primary key autoincrement" +
            "from char(10)" +
            "to char(10)" +
            "msg text" +
            "time char(30))";

    private final String CREATE_BOOK_MSG = "create table tbMsg(" +
            "id integer primary key autoincrement" +
            "name char(10)" +
            "latestMsg text" +
            "latestTime char(30)" +
            "unread integer" +
            "branch char(4) )";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_CHAT);
        db.execSQL(CREATE_BOOK_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
