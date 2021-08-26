package com.muramsyah.hima.voti.core.sf

import android.content.Context
import androidx.core.content.edit

class AppSharedPreference(val context: Context) {

    companion object {
        const val TAG_STARTING = "STARTING"
        const val TAG_STARTED = "IS_STARTED"
    }

    private val sharedPref = context.getSharedPreferences(TAG_STARTING, Context.MODE_PRIVATE)

    fun setIsStarted(isStarted: Boolean) {
        sharedPref.edit {
            putBoolean(TAG_STARTED, isStarted)
            apply()
        }
    }

    fun getIsStarted() = sharedPref.getBoolean("IS_STARTED", false)

}