package domain

import io.ktor.application.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.api.PaginatedRequest
import model.common.Order
import org.koin.ktor.ext.inject

inline fun <reified T: Enum<T>> ApplicationCall.receivePaginatedRequest(): PaginatedRequest<T> {
    val json by inject<Json>()
    val params = request.queryParameters
    return PaginatedRequest(
        page = params["page"]?.toIntOrNull() ?: 0,
        perPage = params["per_page"]?.toIntOrNull() ?: PaginatedRequest.DEFAULT_PER_PAGE,
        order = params["order"]?.runCatching { json.decodeFromString<Order>(this) }?.getOrNull(),
        orderSelect = params["order_select"]?.runCatching { json.decodeFromString<T>(this) }?.getOrNull(),
    )
}