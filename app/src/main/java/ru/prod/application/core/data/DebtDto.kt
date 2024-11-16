package ru.prod.application.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.prod.application.mainMenu.domain.models.DebtModel
import java.math.BigDecimal

@Serializable
data class DebtDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("from_username") val fromUsername: String,
    @SerialName("to_username") val toUsername: String,
    @SerialName("is_closed") val isClosed: Boolean,
    @SerialName("total_amount") val totalAmount: Int,
    @SerialName("payed_amount") val payedAmount: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

fun DebtDto.toDebtModel() = DebtModel(
    id = id,
    name = name,
    isCompleted = isClosed,
    remains = BigDecimal((totalAmount  - payedAmount).toString()),
    fullDebt = BigDecimal("$totalAmount"),
    partnerPhoneNumber = toUsername,
)
