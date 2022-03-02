package model.user

import kotlinx.serialization.SerialName
import model.common.SortSelectable

@kotlinx.serialization.Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String,
): SortSelectable<UserField> {

    override val default = UserField.Id
    override fun select(field: UserField): Comparable<*> = when (field) {
        UserField.Id -> id
        UserField.Email -> email
        UserField.Username -> username
    }
}

enum class UserField { Id, Email, Username }