package ru.prod.application.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appmetrica.analytics.impl.ph
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.prod.application.auth.data.dto.LoginDto
import ru.prod.application.auth.data.dto.LoginResponse
import ru.prod.application.auth.data.dto.SignUpDto
import ru.prod.application.auth.data.dto.SignupResponse
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val httpClient: HttpClient,
) : ViewModel() {
    var loadingStatus by mutableStateOf(LoadingStatus.LOADED)
    var warning by mutableStateOf("")

    fun signUp(phoneNumber: String, password: String, callback: (Token) -> Unit) {
        if (!phoneNumber.matches(Regex("^\\+?[0-9]{10,12}\$"))) {
            warning = "Некорректный формат номера телефона"
            return
        }
        val phoneWithoutPlus = if (phoneNumber.startsWith("+")) phoneNumber.drop(1) else phoneNumber
        warning = ""
        viewModelScope.launch(Dispatchers.IO) {
            loadingStatus = LoadingStatus.LOADING
            val result = safeCall<SignupResponse> {
                httpClient.post(
                    urlString = "$BASE_URL/users"
                ) {
                    setBody(SignUpDto(password = password, username = phoneWithoutPlus, phoneNumber = phoneWithoutPlus))
                }
            }
            when (result) {
                is Result.Error -> {
                    loadingStatus = LoadingStatus.ERROR
                    warning = "Ошибка регистрации"
                }
                is Result.Success -> {
                    login(phoneNumber = phoneNumber, password = password, callback = callback)
                }

            }
        }
    }

    fun login(phoneNumber: String, password: String, callback: (Token) -> Unit) {
        if (phoneNumber.isDigitsOnly().not()) {
            warning = "Проверьте номер телефона"
            return
        }
        warning = ""
        viewModelScope.launch(Dispatchers.IO) {
            loadingStatus = LoadingStatus.LOADING
            val result = safeCall<LoginResponse> {
                httpClient.post(
                    urlString = "$BASE_URL/auth"
                ) {
                    setBody(LoginDto(password = password, username = phoneNumber))
                }
            }
            when (result) {
                is Result.Error -> {
                    loadingStatus = LoadingStatus.ERROR
                    warning = "Ошибка входа"

                }
                is Result.Success -> {
                    loadingStatus = LoadingStatus.LOADED
                    withContext(Dispatchers.Main) {
                        callback(Token(result.data.token))
                    }
                }
            }
        }
    }

}

data class Token(val value: String)
