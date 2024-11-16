package ru.prod.application.mainMenu

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.ui.MainActivity
import ru.prod.application.mainMenu.data.dto.UserResponse
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import javax.inject.Inject

@HiltViewModel
class SharedViewModel  @Inject constructor(
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
) : ViewModel() {

    var username = mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            snapshotFlow {
                authManager.authToken.value
            }.collectLatest { token ->
                setToken(token)
            }
        }
    }

    fun refresh() {
        setToken(authManager.authToken.value)
    }

    private fun setToken(token: String?) {
        if (token == null) {
            username.value = null
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = safeCall<UserResponse> {
                httpClient.get(urlString = "$BASE_URL/users/me") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            when (result) {
                is Result.Error -> {
                    MainActivity.showToast(result.error)
                }
                is Result.Success -> {
                    username.value = result.data.username
                    authManager.setInviteCode(result.data.inviteCode)
                }
            }

        }
    }

}