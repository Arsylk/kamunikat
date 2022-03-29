package model.api.catalog

import kotlinx.serialization.SerialName
import model.common.Sortable

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
) : Sortable<CatalogField> {

    override fun select(field: CatalogField): Comparable<*>? = when (field) {
        CatalogField.Id -> id
        CatalogField.Name -> name
        CatalogField.Letter -> letter
        CatalogField.HasInventory -> hasInventory
    }
}

@kotlinx.serialization.Serializable
enum class CatalogField { Id, Name, Letter, HasInventory }