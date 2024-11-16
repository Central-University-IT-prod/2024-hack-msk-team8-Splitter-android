package ru.prod.application.mainMenu.domain.models

import ru.prod.application.mainMenu.ui.screens.myDebtsScreen.DebtItem

data class GroupInfoModel(
    val name: String,
    val inviteCode: String,
    val debts: List<DebtModel>
)
