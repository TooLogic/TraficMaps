package org.zarroboogs.maps.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.zarroboogs.maps.beans.PaperCameraBean;

import java.util.ArrayList;

/**
 * Created by wangdiyuan on 15-7-14.
 */
public class CameraDao {
    private CameraDbHelper mCameraDbHelper;

    public CameraDao(Context context){
        mCameraDbHelper = new CameraDbHelper(context);
    }

    public ArrayList<PaperCameraBean> selectAllPaperCameras(){
        SQLiteDatabase database = mCameraDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from cameras", null);
        if (cursor != null ){
            ArrayList<PaperCameraBean> list = new ArrayList<>();
            while (cursor.moveToNext()){
                PaperCameraBean paperCameraBean = new PaperCameraBean();
                paperCameraBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                paperCameraBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                paperCameraBean.setDirection(cursor.getString(cursor.getColumnIndex("direction")));
                paperCameraBean.setLongtitude(cursor.getFloat(cursor.getColumnIndex("longtitude")));
                paperCameraBean.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
            }
            return list;
        }
        return null;
    }
}
