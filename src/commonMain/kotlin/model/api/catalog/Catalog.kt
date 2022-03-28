package model.api.catalog

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Catalog(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("letter")
    val letter: Char?,
    @SerialName("has_inventory")
    val hasInventory: Boolean,
)