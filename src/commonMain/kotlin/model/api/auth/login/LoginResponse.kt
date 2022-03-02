package model.api.auth.login

import kotlinx.serialization.SerialName
import model.user.User

@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String,
    @SerialName("user")
    val user: User
)