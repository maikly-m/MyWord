package com.example.mywords.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    //声明数据库帮助器的实例
    public static DBHelper sDBHelper = null;
    //声明数据库的实例
    private SQLiteDatabase db = null;
    //声明数据库的名称
    public static final String DB_NAME = "AppBase.db";
    //声明数据库的版本号
    public static int DB_VERSION = 1;

    public DBHelper (@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper (@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    //利用单例模式获取数据库帮助器的实例
    public static DBHelper getInstance(Context context, int version) {
        if (sDBHelper == null && version > 0) {
            sDBHelper = new DBHelper(context, version);
        } else if (sDBHelper == null) {
            sDBHelper = new DBHelper(context);
        }
        return sDBHelper;
    }

    public boolean hadTable(String table){
        boolean had = false;
        openReadLink();
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+table+"' ";
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    Log.e(TAG, "createWordTable: exist");
                    had = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeLink();
        return had;
    }

    public void createWordTable(Context context){
        //hadTable("wordtable");
        openWriteLink();
        Log.e(TAG, "createWordTable: start");
        //生成表
        InputStream inputStream = null;
        StringBuilder stringBuffer = new StringBuilder();
        try{
            inputStream = context.getAssets().open("word_table.sql");
            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
            String data;
            while ((data=inputStreamReader.readLine()) != null){
                stringBuffer.append(data);
                if (data.endsWith(";")){
                    //插入
                    int length = stringBuffer.length();
                    String trim = stringBuffer.toString().trim();
                    if (!TextUtils.isEmpty(trim)){
                        db.execSQL(trim);
                    }
                    stringBuffer.delete(0, length);
                }
//                if (stringBuffer.length() > 1024*10){
//                    //处理一次数据
//                    String[] split = stringBuffer.toString().split(";");
//                    int length = split.length;
//                    for (int i = 0; i < length - 1; i++){
//                        String trim = split[i].trim();
//                        if (!TextUtils.isEmpty(trim)){
//                            db.execSQL(trim);
//                        }
//                    }
//                    //最后一个放到下一组
//                    stringBuffer.delete(0, length);
//                    stringBuffer.append(split[length-1]);
//                }
            }
//            String trim = stringBuffer.toString().trim();
//            if (!TextUtils.isEmpty(trim)){
//                //最后一次
//                db.execSQL(trim);
//            }
        } catch (IOException e){
            e.printStackTrace();
        }finally{
            if (inputStream != null){
                try{
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, "createWordTable: end");
        closeLink();
    }

    //打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (db == null || !db.isOpen()) {
            db = sDBHelper.getWritableDatabase();
        }
        return db;
    }

    //getWritableDatabase()与getReadableDatabase() 这两个方法都可以获取到数据库的连接
    //正常情况下没有区别，当手机存储空间不够了
    //getReadableDatabase()就不能进行插入操作了，执行插入没有效果
    //getWritableDatabase()：也不能进行插入操作，如果执行插入数据的操作，则会抛异常。对于现在来说不会出现这种情况，用哪种方式都可以

    //打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (db == null || !db.isOpen()) {
            db = sDBHelper.getReadableDatabase();
        }
        return db;
    }

    //关闭数据库的读连接
    public void closeLink() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}