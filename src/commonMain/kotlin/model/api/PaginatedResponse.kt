package model.api

import kotlinx.serialization.SerialName
import model.common.Order
import model.common.SortSelectable

@kotlinx.serialization.Serializable
data class PaginatedResponse<T: Enum<T>, R: SortSelectable<T>>(
    @SerialName("page")
    val page: Int,

    @SerialName("per_page")
    val perPage: Int,

    @SerialName("order")
    val order: Order? = null,

    @SerialName("order_select")
    val orderSelect: T? = null,

    @SerialName("full_size")
    val fullSize: Int,

    @SerialName("list")
    val list: List<R>
)