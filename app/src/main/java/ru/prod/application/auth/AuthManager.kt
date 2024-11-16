package ru.prod.application.auth

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val prefs: SharedPreferences) {

    init {
        INSTANCE = this
    }

    var inviteCode: MutableState<String?> = mutableStateOf<String?>(null)
        private set

    var authToken: MutableState<String?> = mutableStateOf<String?>(null)
        private set

    init {
        authToken.value = prefs.getString("token", null)
        inviteCode.value = prefs.getString("invite_code", null)
    }

    fun setAuthToken(token: String?) {
        authToken.value = token
        prefs.edit().apply {
            putString("token", token)
            remove("invite_code")
        }.apply()
    }

    fun setInviteCode(code: String) {
        inviteCode.value = code
        prefs.edit().apply {
            putString("invite_code", code)
        }.apply()
    }

    companion object {
        lateinit var INSTANCE: AuthManager
    }

}
