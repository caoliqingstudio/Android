package com.example.caoliqing.note_12;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by caoliqing on 2017/3/12.
 */

public class NoteDateBaseHelper extends SQLiteOpenHelper {

    public static final String CreateNote = "create table note ("
            + "id integer primary key autoincrement, "
            + "content text , "
            + "date text)";

    public NoteDateBaseHelper(Context context) {
        super(context, "note", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}