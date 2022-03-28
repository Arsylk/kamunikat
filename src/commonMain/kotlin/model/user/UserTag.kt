package model.user

import kotlinx.serialization.SerialName
import model.common.Sortable

@kotlinx.serialization.Serializable
data class UserTag(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
) : Sortable<UserTagField> {

    override fun select(field: UserTagField): Comparable<*> = when (field) {
        UserTagField.Id -> id
        UserTagField.Name -> name
    }
}

@kotlinx.serialization.Serializable
enum class UserTagField {
    Id, Name
}