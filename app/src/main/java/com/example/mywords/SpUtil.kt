package com.example.mywords

import android.content.Context
import android.content.Context.MODE_PRIVATE


object SpUtil{
    val spName = "words"
    fun putString(context: Context,key: String, value: String){
        context.getSharedPreferences(spName, MODE_PRIVATE).let {
            it.edit().putString(key, value).commit()
        }
    }

    fun getString(context: Context,key: String): String?{
        context.getSharedPreferences(spName, MODE_PRIVATE).let {
            return it.getString(key, "")
        }
    }

}