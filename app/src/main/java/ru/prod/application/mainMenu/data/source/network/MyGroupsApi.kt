package ru.prod.application.mainMenu.data.source.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.data.DebtDto
import ru.prod.application.mainMenu.domain.models.GroupModel
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import java.math.BigDecimal
import javax.inject.Inject

// Класс для работы с апи с долгами пользователя
class MyGroupsApi @Inject constructor(
    private val httpClient: HttpClient,
    private val authManager: AuthManager
) {
    suspend fun getMyGroups(): Result<List<GroupDto>> {
        val token = authManager.authToken.value
            ?: return Result.Error("Не авторизован")
        val result = safeCall<List<GroupDto>> {
            httpClient.get(
                urlString = "$BASE_URL/groups/created"
            ) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return result
    }
}

@Serializable
data class GroupDto(
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("id")
    val id: Int,
    @SerialName("invite_code")
    val inviteCode: String,
    @SerialName("name")
    val name: String
)

fun GroupDto.toGroupModel(): GroupModel {
    return GroupModel(
        id = id,
        name = name,
        inviteCode = inviteCode,
        isCompleted = false,
    )
}

