package com.joao.fulgencio.fragmentos.session

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val SESSION_PREF = "session_pref"
    private const val LOGGED_IN = "logged_in"
    private const val LOGIN_TIME = "login_time"
    private const val SESSION_DURATION = 15 * 60 * 1000 // 15 minutos

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(SESSION_PREF, Context.MODE_PRIVATE)
    }

    fun saveSessionData() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGGED_IN, true)
        editor.putLong(LOGIN_TIME, System.currentTimeMillis())
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        val loggedIn = sharedPreferences.getBoolean(LOGGED_IN, false)
        val loginTime = sharedPreferences.getLong(LOGIN_TIME, 0)
        val currentTime = System.currentTimeMillis()
        return loggedIn && (currentTime - loginTime < SESSION_DURATION)
    }
}
