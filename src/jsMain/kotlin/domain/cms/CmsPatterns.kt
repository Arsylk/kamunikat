package domain.cms

import io.ktor.client.*
import io.ktor.client.request.*
import model.api.PaginatedResponse
import model.api.SuccessResponse
import model.common.Order
import model.common.Sortable


inline fun <reified T: Any> HttpClient.cmsPattern(baseUrl: String): CmsPattern<T> {
    return object : CmsPattern<T> {
        override suspend fun getList(): List<T> = get<List<T>>("$baseUrl/list")

        override suspend fun get(id: Int): T = get<T>("$baseUrl/$id")

        override suspend fun add(item: T): T = post<T>(baseUrl) { body = item }

        override suspend fun update(id: Int, item: T): SuccessResponse = put("$baseUrl/$id") { body = item }

        override suspend fun delete(id: Int): SuccessResponse = delete("$baseUrl/$id")
    }
}
inline fun <reified Item: Sortable<Field>, Field: Enum<Field>> HttpClient.paginatedCmsPattern(
    baseUrl: String
): PaginatedCmsPattern<Item, Field> {
    return object : PaginatedCmsPattern<Item, Field>, CmsPattern<Item> by cmsPattern(baseUrl) {
        override suspend fun getPaginated(
            page: Int,
            perPage: Int,
            order: Order?,
            orderSelect: Field?
        ) = get<PaginatedResponse<Item, Field>>("$baseUrl/paginated") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("order", order)
            parameter("order_select", orderSelect)
        }
    }
}
