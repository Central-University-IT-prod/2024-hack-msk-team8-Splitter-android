package ru.prod.application.mainMenu.ui.screens.myGroupsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.mainMenu.data.source.network.MyDebtsApi
import ru.prod.application.mainMenu.data.source.network.MyGroupsApi
import ru.prod.application.mainMenu.data.source.network.toGroupModel
import ru.prod.application.mainMenu.domain.models.GroupModel
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MyGroupsViewModel @Inject constructor(
    private val myGroupsApi: MyGroupsApi,
    private val myDebtsApi: MyDebtsApi,
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
) : ViewModel() {
    var loadingString by mutableStateOf(LoadingStatus.LOADING)
        private set
    var myGroups by mutableStateOf<List<GroupModel>?>(null)
        private set

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingString = LoadingStatus.LOADING
            try {
                val result = myGroupsApi.getMyGroups()
                when (result) {
                    is Result.Error -> {
                        error.value = result.error
                    }
                    is Result.Success -> {
                        myGroups = result.data.map { it.toGroupModel() }
                    }
                }
                loadingString = LoadingStatus.LOADED
            } catch (e: Exception) {
                loadingString = LoadingStatus.ERROR
            }
        }
    }

    // Состояние модалки
    var error = mutableStateOf<String?>(null)
        private set

    var showModal by mutableStateOf(false)

    fun save(name: String) {
        if (name.isBlank()) {
            error.value = "Неправильное название"
            return
        }
        error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            val response = safeCall<HttpResponse> {
                httpClient.post("$BASE_URL/groups") {
                    val token = authManager.authToken.value
                    setBody(NewGroupDto(name))
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            when (response) {
                is Result.Error -> {
                    error.value = "Ошибка создания"
                }

                is Result.Success -> {
                    showModal = false
                    loadData()
                }
            }
        }
    }

    fun refresh() = loadData()
}

@Serializable
data class NewGroupDto(
    @SerialName("name")
    val name: String,
)