package com.gert.tfgdam.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gert.tfgdam.model.JwtPayload
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Base64

object JwtManager {

    private val Context.dataStore by preferencesDataStore("jwt_prefs")
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val gson = Gson()

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    fun getToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    fun getUserInfoFromToken(token: String): JwtPayload? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            gson.fromJson(payloadJson, JwtPayload::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getUserInfoFlow(context: Context): Flow<JwtPayload?> {
        return getToken(context).map { token ->
            token?.let { getUserInfoFromToken(it) }
        }
    }
}