package domain.api

import io.ktor.client.*
import io.ktor.client.request.*
import model.Author
import model.api.PaginatedResponse
import model.api.SuccessResponse
import model.api.auth.login.LoginRequest
import model.api.auth.login.LoginResponse
import model.common.Order
import model.user.User
import model.user.UserField

class ApiService(private val httpClient: HttpClient) {

    suspend fun login(email: String, password: String) =
        httpClient.post<LoginResponse>("/api/auth/login") {
            body = LoginRequest(email, password)
        }

    suspend fun logout() = httpClient.post<SuccessResponse>("/api/auth/logout")


    suspend fun getAuthors() = httpClient.get<List<Author>>("/api/authors")

    suspend fun getAuthor(id: Int) = httpClient.get<Author>("/api/author/$id")

    suspend fun putAuthor(author: Author) = httpClient.put<SuccessResponse>("/api/author") {
        body = author
    }

    suspend fun getUsers(page: Int, perPage: Int, order: Order?, orderSelect: UserField?) = httpClient.get<PaginatedResponse<UserField, User>> {
        parameter("page", page)
        parameter("per_page", perPage)
        parameter("order", "asc")
        parameter("order_select", orderSelect)
    }
}