package model.api

import kotlinx.serialization.SerialName
import model.common.Order

@kotlinx.serialization.Serializable
data class PaginatedRequest<T: Enum<T>>(
    @SerialName("page")
    val page: Int,

    @SerialName("per_page")
    val perPage: Int,

    @SerialName("order")
    val order: Order? = null,

    @SerialName("order_select")
    val orderSelect: T? = null,
) {

    companion object {
        const val DEFAULT_PER_PAGE = 25
    }
}