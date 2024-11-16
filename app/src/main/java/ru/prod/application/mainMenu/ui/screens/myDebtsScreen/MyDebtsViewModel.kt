package ru.prod.application.mainMenu.ui.screens.myDebtsScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.data.toDebtModel
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.core.ui.MainActivity
import ru.prod.application.mainMenu.data.source.network.MyDebtsApi
import ru.prod.application.mainMenu.domain.models.DebtModel
import ru.prod.application.mainMenu.ui.views.statisticsView.DebtStats
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MyDebtsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myDebtsApi: MyDebtsApi,
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) : ViewModel() {
    var loadingString by mutableStateOf(LoadingStatus.LOADING)
        private set
    var myDebts by mutableStateOf<List<DebtItem>?>(null)
        private set

    var bannerViewPosition by mutableIntStateOf(2)
    var showAddDebtModal by mutableStateOf(false)
    var error = mutableStateOf<String?>(null)
        private set

    fun save(code: String) {
        if (code.isBlank()) {
            error.value = "Неправильный код"
            return
        }
        error.value = null
        viewModelScope.launch {
            val response = safeCall<HttpResponse> {
                httpClient.post("$BASE_URL/groups/$code/join") {
                    val token = authManager.authToken.value
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            when (response) {
                is Result.Error -> {
                    MainActivity.showToast(response.error)
                    error.value = "Ошибка присоединения"
                }

                is Result.Success -> {
                    showAddDebtModal = false
                    MainActivity.showToast("Вы присоединились к группе")
                    loadData()
                }
            }
        }
    }

    fun saveDebtById(id: Int, newDebt: BigDecimal) {
        viewModelScope.launch {
            val response = safeCall<HttpResponse> {
                httpClient.put("$BASE_URL/debts/$id/payedAmount/increase") {
                    val token = authManager.authToken.value
                    header(HttpHeaders.Authorization, "Bearer $token")
                    setBody(mapOf("amount" to newDebt))
                }
            }
            when (response) {
                is Result.Success -> {
                    myDebts = myDebts?.map { debtItem ->
                        if (debtItem is DebtItem.Debt && debtItem.debtModel.id == id) {
                            debtItem.copy(debtModel = debtItem.debtModel.copy(remains = debtItem.debtModel.fullDebt - newDebt))
                        } else {
                            debtItem
                        }
                    }
                    showAddDebtModal = false
                    loadData()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Ошибка пополнения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                val result = myDebtsApi.getMyDebts()
                when (result) {
                    is Result.Error -> {
                        MainActivity.showToast(result.error)
                        loadingString = LoadingStatus.ERROR
                    }
                    is Result.Success -> {
                        myDebts = result.data.map {
                            DebtItem.Debt(it.toDebtModel()) as DebtItem
                        }.toMutableList().apply {
                            add(result.data.indices.random(), DebtItem.Banner)
                        }
                    }
                }
                bannerViewPosition = (Math.random() * (myDebts!!.size - 2) + 2).toInt() - 1
            } catch (e: Exception) {
                loadingString = LoadingStatus.ERROR
            }
            loadingString = LoadingStatus.LOADED
        }
    }
}

sealed class DebtItem {
    data object Banner : DebtItem()
    data class Debt(val debtModel: DebtModel) : DebtItem()
}
