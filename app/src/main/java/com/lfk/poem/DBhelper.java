package com.lfk.poem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/5/8.
 */
public class DBhelper extends SQLiteOpenHelper {
    private  static final String CREAT_DB = "create table book ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "poem text)";
    private Context mcontext;

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_DB);
        Toast.makeText(mcontext,"succeed collect！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
