package model.api.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddUserRequest(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("tag_ids")
    val tagIds: Set<Int>,
)