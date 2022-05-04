package com.lakbay.pamayanan.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtils {

    companion object {
        private val PREF_APP = "lakbay"

        fun saveData(context: Context, key: String, value: String) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply()
        }

        fun saveData(context: Context, key: String, value: Boolean) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply()
        }

        fun saveData(context: Context, key: String, value: Float) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, value)
                .apply()
        }

        fun saveData(context: Context, key: String, value: Int) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply()
        }

        fun saveData(context: Context, key: String, value: Long) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .apply()
        }

        fun getStringData(context: Context, key: String): String? {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .getString(key, "")
        }

        fun getBooleanData(context: Context, key: String): Boolean {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .getBoolean(key, false)
        }

        fun getLongData(context: Context, key: String): Long {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .getLong(key, 0L)
        }

        fun getIntData(context: Context, key: String): Int {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .getInt(key, 0)
        }

        fun getFloatData(context: Context, key: String): Float {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .getFloat(key, 0f)
        }
    }

}