package com.example.listacomprasinteligente

import android.content.Context

class SecurityPreferences(context: Context) {
    private val shared = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        shared.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return shared.getString(key, "") ?: ""
    }
}
