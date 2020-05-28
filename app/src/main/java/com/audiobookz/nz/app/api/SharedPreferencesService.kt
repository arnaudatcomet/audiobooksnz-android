package com.audiobookz.nz.app.api

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesService(var context: Context) {

    var share_preferen_token_key: String = "Token"

    private val shrPref: SharedPreferences =
        context.getSharedPreferences(share_preferen_token_key, Context.MODE_PRIVATE)

    fun saveToken(accessToken: String) {
        shrPref.edit().putString(share_preferen_token_key, accessToken).apply()
    }

    fun deleteToken() {
        shrPref.edit().remove(share_preferen_token_key).apply()
    }

    fun getToken(): String? = shrPref.getString(share_preferen_token_key, null)
}