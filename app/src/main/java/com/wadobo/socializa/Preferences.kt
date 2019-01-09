package com.wadobo.socializa

import android.content.Context
import android.content.SharedPreferences


class Preferences(context: Context) {
    val PREFS_NAME = "socializa"
    val ACCESS_TOKEN = "access_token"
    val CLIENT_ID = "client_id"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var access_token: String
        get() = prefs.getString(ACCESS_TOKEN, "")
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    var client_id: String
        get() = prefs.getString(CLIENT_ID, "")
        set(value) = prefs.edit().putString(CLIENT_ID, value).apply()

    init {
        prefs.edit().putString(CLIENT_ID, "z5SCIpsCZR1wnPlJFZtDkNmGVBQpViWMRx5aKIV9").apply()
    }
}