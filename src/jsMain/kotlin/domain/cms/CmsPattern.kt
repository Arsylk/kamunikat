package domain.cms

import io.ktor.client.*
import io.ktor.client.request.*
import model.api.SuccessResponse

interface CmsPattern<T: Any> {

    suspend fun getList(): List<T>

    suspend fun get(id: Int): T

    suspend fun add(item: T): SuccessResponse

    suspend fun update(item: T): SuccessResponse

    suspend fun delete(id: Int): SuccessResponse
}

inline fun <reified T: Any> HttpClient.cmsPattern(baseUrl: String): CmsPattern<T> {
    return object : CmsPattern<T> {
        override suspend fun getList(): List<T> = get<List<T>>("$baseUrl/list")

        override suspend fun get(id: Int): T = get<T>("$baseUrl/$id")

        override suspend fun add(item: T): SuccessResponse = post(baseUrl) { body = item }

        override suspend fun update(item: T): SuccessResponse = put(baseUrl) { body = item }

        override suspend fun delete(id: Int): SuccessResponse = delete("$baseUrl/$id")
    }
}