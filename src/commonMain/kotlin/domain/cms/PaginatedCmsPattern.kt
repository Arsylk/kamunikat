package domain.cms

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