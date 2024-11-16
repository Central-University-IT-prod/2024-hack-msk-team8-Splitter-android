package ru.prod.application.mainMenu.ui.screens.groupInfoSreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.mainMenu.data.source.network.GroupInfoApi
import ru.prod.application.mainMenu.domain.models.DebtModel
import ru.prod.application.mainMenu.domain.models.GroupInfoModel
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class GroupInfoScreenViewModel @Inject constructor(
    private val groupInfoApi: GroupInfoApi,
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) : ViewModel() {
    var loadingStatus by mutableStateOf(LoadingStatus.LOADING)
        private set

    var groupId by mutableStateOf<Int?>(null)
    var groupInfo by mutableStateOf<GroupInfoModel?>(null)

    var showAddDebtModal by mutableStateOf(false)

    var users by mutableStateOf<List<UserInfo>?>(null)

    fun loadGroupInfo(
        id: Int,
        groupName: String,
        inviteCode: String,
    ) {
        viewModelScope.launch {
            groupId = id
            loadingStatus = LoadingStatus.LOADING
            val token = authManager.authToken.value
            val response1 = safeCall<List<UserInfo>> {
                httpClient.get(
                    urlString = "$BASE_URL/groups/$id/users"
                ) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            val response2 = safeCall<List<Debt>> {
                httpClient.get(
                    urlString = "$BASE_URL/groups/$id/debts/incoming"
                ) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            if (response1 is Result.Success && response2 is Result.Success) {
                users = response1.data
                groupInfo = GroupInfoModel(
                    groupName,
                    inviteCode,
                    response2.data.map { it.toDebtModel() }
                )

                loadingStatus = LoadingStatus.LOADED
            } else {
                loadingStatus = LoadingStatus.ERROR
            }
        }
    }

    var error by mutableStateOf<String?>(null)
        private set

    fun save(name: String, fullDebt: String, user: UserInfo) {
        if (name.isBlank()) {
            error = "Неправильное название"
            return
        }
        if (fullDebt.toIntOrNull() == null) {
            error = "Неправильное значение долга"
            return
        }
        error = null
        viewModelScope.launch {
            val requestBody = AddDebtRequest(
                group_id = groupId!!,
                invite_code = user.invite_code,
                name = name,
                to_username = user.username,
                total_amount = fullDebt.toInt()
            )

            val response = safeCall<HttpResponse> {
                httpClient.post("$BASE_URL/debts") {
                    val token = authManager.authToken.value
                    header(HttpHeaders.Authorization, "Bearer $token")
                    setBody(requestBody)
                }
            }


            when (response) {
                is Result.Error -> {
                    error = "Ошибка создания"
                }

                is Result.Success -> {
                    showAddDebtModal = false
                    loadGroupInfo(groupId!!, groupInfo!!.name, groupInfo!!.inviteCode)
                }
            }
        }
    }

    fun saveDebtById(id: Int, newDebt: BigDecimal) {
        viewModelScope.launch {
            val response = safeCall<HttpResponse> {
                httpClient.patch("$BASE_URL/debts/$id/close") {
                    val token = authManager.authToken.value
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            when (response) {
                is Result.Success -> {
                    groupInfo = groupInfo?.copy(
                        debts = groupInfo?.debts?.map { debt ->
                            if (debt.id == id) {
                                debt.copy(
                                    remains = BigDecimal.ZERO
                                )
                            } else debt
                        } ?: emptyList()
                    )
                    showAddDebtModal = false
                }

                is Result.Error -> {
                    error = "Ошибка пополнения"
                }
            }
        }
    }
}

@Serializable
data class UserInfo(
    val username: String,
    val invite_code: String
)

@Serializable
data class Debt(
    val id: Int,
    val name: String,
    val is_closed: Boolean,
    val payed_amount: Long,
    val total_amount: Long,
    val from_username: String,
) {
    fun toDebtModel(): DebtModel {
        return DebtModel(
            id,
            name,
            is_closed,
            BigDecimal(total_amount)-BigDecimal(payed_amount),
            BigDecimal(total_amount),
            from_username
        )
    }
}


@Serializable
data class AddDebtRequest(
    val group_id: Int,
    val invite_code: String,
    val name: String,
    val to_username: String,
    val total_amount: Int
)