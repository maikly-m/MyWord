/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mywords.sql;

import android.content.Context;
import android.util.Log;

import com.example.mywords.sql.dao.WordTableDao;
import com.example.mywords.sql.entity.WordTableEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WordTableEntity.class,}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "AppBase.db";

    public abstract WordTableDao wordTableDao ();


    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance (final Context context) {
        if (sInstance == null){
            synchronized (AppDatabase.class){
                if (sInstance == null){
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase (final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate (@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.e(TAG, "onCreate: databaseBuilder " );
                        //建表
                        new Thread(() -> {
                            String s = "DROP TABLE IF EXISTS wordtable;";
                            db.execSQL(s);
                            s = "CREATE TABLE wordtable (id integer primary key autoincrement,etymology text, " +
                                    "example text, exampletranslation text, [explain] text, iscollect integer, islearned " +
                                    "integer, issimple integer, level text, meaning text, rootvariant text, soundmark text," +
                                    " structure text, translation text, type text, type1 text, type2 text, type3 text, " +
                                    "type4 text, word text, wordid text);";
                            db.execSQL(s);
                            //db.query("");
                            //插入数据
                            DBHelper.getInstance(appContext,2).createWordTable(appContext);
                        }).start();
                    }
                })
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated (final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()){
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated () {
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData (final AppDatabase database, final List<WordTableEntity> wordTableEntityList) {
    }

    private static void addDelay () {
        try{
            Thread.sleep(4000);
        } catch (InterruptedException ignored){
        }
    }

    public LiveData<Boolean> getDatabaseCreated () {
        return mIsDatabaseCreated;
    }
}