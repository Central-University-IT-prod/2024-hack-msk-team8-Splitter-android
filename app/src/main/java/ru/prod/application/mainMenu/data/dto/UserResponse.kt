package ru.prod.application.mainMenu.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("username")
    val username: String,
    @SerialName("invite_code")
    val inviteCode: String,
)
