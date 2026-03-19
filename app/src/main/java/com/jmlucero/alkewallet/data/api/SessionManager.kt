package com.jmlucero.alkewallet.data.api

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val LOGGED_USER_EMAIL= "logged_user_email"
    }

    fun saveEmail(email: String) {
        prefs.edit().putString(LOGGED_USER_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString(LOGGED_USER_EMAIL, null)
    }

    fun clearEmail() {
        prefs.edit().remove(LOGGED_USER_EMAIL).apply()
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}