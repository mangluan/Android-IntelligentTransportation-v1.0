package com.icuter.shiti1.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.icuter.shiti1.MainFragment.BillManage;
import com.icuter.shiti1.MainFragment.HLManage;
import com.icuter.shiti1.MainFragment.PM2_5Index;
import com.icuter.shiti1.MainFragment.RealTimeShowViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 50834 on 2018/12/5.
 */

public class MySQLite extends SQLiteOpenHelper {

    private static int VERSION = 3;

    public MySQLite(Context context, String name) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table ChongZhiJiLu (carID ,money , UserName , time );");
        sqLiteDatabase.execSQL("Create table HongLvDENG (_id integer,Hong integer, Huang integer, Lv integer);");
        sqLiteDatabase.execSQL("Create table HuanJingZhiShu (name text,_index integer, mTime long);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public <T> List<T> Query(String strSQL){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL,null);
        List<T> data = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()) {

            }
        }cursor.close();
        return data;
    }

    public void ExecSQL(String strSQL , String...values){
        SQLiteDatabase db = getReadableDatabase();
        if (values==null){
            db.execSQL(strSQL);
        }else {
            db.execSQL(strSQL,values);
        }
    }

    public List<RealTimeShowViewPager.MyData> ShowQuery(String strSQL){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL,null);
        List<RealTimeShowViewPager.MyData> data = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int index = cursor.getInt(cursor.getColumnIndex("_index"));
                long time = cursor.getLong(cursor.getColumnIndex("mTime"));
                data.add(new RealTimeShowViewPager.MyData(name,index,time));
            }
        }cursor.close();
        return data;
    }

    public List<PM2_5Index.ListData> PmQuery(String strSQL){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL,null);
        List<PM2_5Index.ListData> data = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int index = cursor.getInt(cursor.getColumnIndex("_index"));
                data.add(new PM2_5Index.ListData(name,index));
            }
        }cursor.close();
        return data;
    }

    public List<BillManage.ListData> BillQuery(String strSQL){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL,null);
        List<BillManage.ListData> data = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()) {
                int carID = cursor.getInt(cursor.getColumnIndex("carID"));
                int money = cursor.getInt(cursor.getColumnIndex("money"));
                String name = cursor.getString(cursor.getColumnIndex("UserName"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                data.add(new BillManage.ListData(carID,money,name,time));
            }
        }cursor.close();
        return data;
    }

    public List<HLManage.ListData> HLQuery(String strSQL){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL,null);
        List<HLManage.ListData> data = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                int Hong = cursor.getInt(cursor.getColumnIndex("Hong"));
                int Huang = cursor.getInt(cursor.getColumnIndex("Huang"));
                int Lv = cursor.getInt(cursor.getColumnIndex("Lv"));
                data.add(new HLManage.ListData(_id,Hong,Huang,Lv));
            }
        }cursor.close();
        return data;
    }

}
