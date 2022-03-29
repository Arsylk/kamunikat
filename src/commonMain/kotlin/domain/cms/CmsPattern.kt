package domain.cms

import model.api.SuccessResponse

interface CmsPattern<T: Any> {

    suspend fun getList(): List<T>

    suspend fun get(id: Int): T

    suspend fun add(item: T): T

    suspend fun update(id: Int, item: T): SuccessResponse

    suspend fun delete(id: Int): SuccessResponse
}