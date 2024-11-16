package ru.prod.application.mainMenu.ui.views.statisticsView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.core.ui.MainActivity
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) : ViewModel() {
    var loadingStatus by mutableStateOf(LoadingStatus.LOADING)
        private set
    var outcomeProgress by mutableIntStateOf(0)
    var incomeProgress by mutableIntStateOf(0)

    fun loadData() {
        viewModelScope.launch {
            loadingStatus = LoadingStatus.LOADING

            val token = authManager.authToken.value
            val response = safeCall<DebtStats> {
                httpClient.get(
                    urlString = "$BASE_URL/debts/stats"
                ) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            when (response) {
                is Result.Error -> {
                    MainActivity.showToast(response.error)
                    loadingStatus = LoadingStatus.ERROR
                }

                is Result.Success -> {
                    if (response.data.unpayed_outcoming_debts_amount == 0) {
                        outcomeProgress = 100
                    } else {
                        outcomeProgress =
                            ((response.data.payed_outcoming_debts_amount / response.data.unpayed_outcoming_debts_amount).toFloat() * 100).roundToInt()
                    }
                    if (response.data.unpayed_incoming_debts_amount == 0) {
                        incomeProgress = 100
                    } else {
                        incomeProgress =
                            ((response.data.payed_incoming_debts_amount / response.data.unpayed_incoming_debts_amount).toFloat() * 100).roundToInt()
                    }
                }
            }

            loadingStatus = LoadingStatus.LOADED
        }
    }
}

@Serializable
data class DebtStats(
    val payed_incoming_debts_amount: Int,
    val payed_outcoming_debts_amount: Int,
    val unpayed_incoming_debts_amount: Int,
    val unpayed_outcoming_debts_amount: Int
)