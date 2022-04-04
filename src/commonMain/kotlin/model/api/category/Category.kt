package model.api.category

import kotlinx.serialization.SerialName
import model.common.IntId

@kotlinx.serialization.Serializable
data class Category(
    @SerialName("id")
    override val id: Int,
    @SerialName("name")
    val name: String,
): IntId