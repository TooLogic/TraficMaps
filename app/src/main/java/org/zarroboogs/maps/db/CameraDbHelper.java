package org.zarroboogs.maps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangdiyuan on 15-7-14.
 */
public class CameraDbHelper extends SQLiteOpenHelper {
    private CameraDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CameraDbHelper(Context context){
        super(context, "cameras.db", null, 21);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("create table cameras (id integer PRIMARY KEY, name VARCHAR(20), direction VARCHAR(100), longtitude DOUBLE, latitide DOUBLE, ntrigger integer, note VARCHAR(200), workmode text, district VARCHAR(10), looproad vchar(6),type INT);");
        String a = "CREATE TABLE [cameras] ( [id] integer PRIMARY KEY AUTOINCREMENT, [name] varchar(20) NOT NULL ON CONFLICT FAIL CONSTRAINT [Name_Not_Same] UNIQUE ON CONFLICT FAIL, [direction] varchar(100), [longtitude] double, [latitude] double, [ntrigger] int DEFAULT 1, [note] varchar(200), [workmode] string DEFAULT fulltime, [district] VARCHAR(10), [looproad] vchar(6), [type] INT NOT NULL DEFAULT 0);";
        sqLiteDatabase.execSQL(a);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
