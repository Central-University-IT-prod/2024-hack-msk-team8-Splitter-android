package ru.prod.application.mainMenu.ui.views.debtView

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.prod.application.auth.AuthManager
import ru.prod.application.mainMenu.domain.models.DebtModel
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import javax.inject.Inject

@HiltViewModel
class DebtViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) : ViewModel() {
}