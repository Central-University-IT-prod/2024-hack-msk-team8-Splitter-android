package ru.prod.application.mainMenu.domain.models

import java.math.BigDecimal

data class GroupModel(
    val id: Int,
    val name: String,
    val inviteCode: String,
    val isCompleted: Boolean,
    //val remains: BigDecimal,
    //val fullDebt: BigDecimal,
    //val participantsCount: Int
)
