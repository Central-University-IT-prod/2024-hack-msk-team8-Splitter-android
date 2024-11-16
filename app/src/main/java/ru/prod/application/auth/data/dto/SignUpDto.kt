package ru.prod.application.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    @SerialName("password")
    val password: String,
    @SerialName("username")
    val username: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
)
