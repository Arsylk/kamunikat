package domain.api

import io.ktor.client.*
import io.ktor.client.request.*
import model.Author
import model.api.PaginatedResponse
import model.api.SuccessResponse
import model.api.auth.login.LoginRequest
import model.api.auth.login.LoginResponse
import model.api.user.AddUserRequest
import model.common.Order
import model.user.User
import model.user.UserField
import model.user.UserTag

class ApiService(private val httpClient: HttpClient) {

    suspend fun login(email: String, password: String) =
        httpClient.post<LoginResponse>("/api/auth/login") {
            body = LoginRequest(email, password)
        }

    suspend fun logout() = httpClient.post<SuccessResponse>("/api/auth/logout")

    suspend fun getUsers(page: Int, perPage: Int, order: Order?, orderSelect: UserField?) =
        httpClient.get<PaginatedResponse<UserField, User>>("/api/users") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("order", order)
            parameter("order_select", orderSelect)
        }

    suspend fun addUser(request: AddUserRequest) =
        httpClient.post<SuccessResponse>("/api/user") {
            body = request
        }


    suspend fun getUserTags() =
        httpClient.get<List<UserTag>>("/api/user-tags")

    suspend fun addUserTag(tag: UserTag) =
        httpClient.post<SuccessResponse>("/api/user-tag") {
            body = tag
        }
}