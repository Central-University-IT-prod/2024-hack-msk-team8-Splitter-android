package ru.prod.application.mainMenu.data.source.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.data.DebtDto
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import javax.inject.Inject

// Класс для работы с апи с долгами пользователя
class MyDebtsApi @Inject constructor(
    private val authManager: AuthManager,
    private val httpClient: HttpClient
) {
    suspend fun getMyDebts(): Result<List<DebtDto>> {
        val token = authManager.authToken.value
            ?: return Result.Error("Не авторизован")
        val result = safeCall<List<DebtDto>> {
            httpClient.get(
                urlString = "$BASE_URL/debts/outcoming"
            ) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return result
    }

    suspend fun createDebt(name: String, inviteCode: String, amount: String): Result<DebtDto> {
        val token = authManager.authToken.value
            ?: return Result.Error("Не авторизован")
        val result = safeCall<DebtDto> {
            httpClient.post(
                urlString = "$BASE_URL/debts"
            ) {
                setBody(CreateDebtDto(name = name, inviteCode = inviteCode, totalAmount = amount.toLong()))
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return result
    }
}

@Serializable
data class CreateDebtDto(
    @SerialName("name")
    val name: String,
    @SerialName("invite_code")
    val inviteCode: String,
    @SerialName("total_amount")
    val totalAmount: Long,
)
