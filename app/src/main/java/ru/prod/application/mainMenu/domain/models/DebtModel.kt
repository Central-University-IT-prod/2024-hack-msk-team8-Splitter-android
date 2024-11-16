package ru.prod.application.mainMenu.domain.models

import java.math.BigDecimal

data class DebtModel(
    val id: Int,
    val name: String,
    val isCompleted: Boolean,
    val remains: BigDecimal,
    val fullDebt: BigDecimal,
    val partnerPhoneNumber: String,
)
