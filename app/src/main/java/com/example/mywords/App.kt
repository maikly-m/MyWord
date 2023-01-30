package com.example.mywords

import android.app.Application
import android.util.Log
import com.example.mywords.sql.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            Log.e("App", "onCreate: " )
            AppDatabase.getInstance(this@App).wordTableDao()
        }
    }
}