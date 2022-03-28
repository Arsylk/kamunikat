package domain.cms

import io.ktor.client.*
import io.ktor.client.request.*
import model.api.PaginatedResponse
import model.common.Order
import model.common.Sortable

interface PaginatedCmsPattern<Item: Sortable<Field>, Field: Enum<Field>> : CmsPattern<Item> {
    suspend fun getPaginated(
        page: Int,
        perPage: Int,
        order: Order? = null,
        orderSelect: Field? = null,
    ): PaginatedResponse<Item, Field>
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