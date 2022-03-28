package model.api

import kotlinx.serialization.SerialName
import model.common.EmptyEnum
import model.common.EmptySortable
import model.common.Order
import model.common.Sortable

@kotlinx.serialization.Serializable
data class PaginatedResponse<Item: Sortable<Field>, Field: Enum<Field>>(
    @SerialName("page")
    val page: Int,

    @SerialName("per_page")
    val perPage: Int,

    @SerialName("order")
    val order: Order? = null,

    @SerialName("order_select")
    val orderSelect: Field? = null,

    @SerialName("full_size")
    val fullSize: Int,

    @SerialName("items")
    val items: List<Item>
)