package ru.prod.application.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import ru.prod.application.auth.AuthManager
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return Result.Error("Нет доступа к интернету")
    } catch(e: SerializationException) {
        return Result.Error("Ошибка сериализации")
    } catch(e: io.ktor.serialization.JsonConvertException) {
        return Result.Error("Ошибка")
    } catch(e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(null)
    }

    val ok = listOf(HttpStatusCode.OK, HttpStatusCode.Created)

    if (response.status == HttpStatusCode.Forbidden || response.status == HttpStatusCode.Unauthorized) {
        AuthManager.INSTANCE.setAuthToken(null)
    }

    if (response.status !in ok) {
        return Result.Error("Ошибка ${response.status.value}")
    }

    return Result.Success(response.body())
}

typealias NetworkError = String?

sealed interface Result<out D> {
    data class Success<out D>(val data: D): Result<D>
    data class Error(val error: NetworkError?): Result<Nothing>
}

const val BASE_URL = "http://{{sensitive_data}}:8000/api/v1"
