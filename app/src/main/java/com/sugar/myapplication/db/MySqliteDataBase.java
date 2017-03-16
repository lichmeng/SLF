package com.sugar.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sugar.myapplication.App;
import com.sugar.myapplication.bean.SearchRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjx on 2016/11/23.
 * crud搜索记录
 */

public class MySqliteDataBase {
    private static final String TAG = "MySqliteDataBase";
    private static final String  SEARCH_RECORD ="search" ;
    private final MyOpenHelper mHelper;

    public MySqliteDataBase(){
        mHelper = new MyOpenHelper(App.mContext);
    }

    /**
     * 添加搜索记录
     * @param record
     */
    public boolean addSearchRecord(SearchRecord record){
        ContentValues values = new ContentValues();
        values.put("userid",record.getUserId());
        values.put("name",record.getSearchName());
        values.put("count",record.getSearchCount());
        values.put("time",record.getSearchTime());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long result = db.insert(SEARCH_RECORD, null, values);
        db.close();
        if (result>0) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据userid查询搜索记录,按照时间排序
     * @param userid
     */
    public List<String> query(String userid){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(SEARCH_RECORD, new String[]{"name"}, "userid=?", new String[]{userid}, null, null, "time ASC");
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            list.add(name);
        }
        db.close();
        return list;
    }

    /**
     * 根据userid删除
     * @param userId
     * @return
     */
    public boolean delete(String userId){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete(SEARCH_RECORD, "userid=?", new String[]{userId});
        if (delete>0) {
            return true;
        }
        return false;
    }

}
