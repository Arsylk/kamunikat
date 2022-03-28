package model.user

import kotlinx.serialization.SerialName
import model.common.Sortable

@kotlinx.serialization.Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String,
    @SerialName("tags")
    val tags: List<UserTag>,
): Sortable<UserField> {

    override fun select(field: UserField): Comparable<*> = when (field) {
        UserField.Id -> id
        UserField.Email -> email
        UserField.Username -> username
    }
}

@kotlinx.serialization.Serializable
enum class UserField { Id, Email, Username }