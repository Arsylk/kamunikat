package route.api.auth

import domain.auth.AuthService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import model.api.SuccessResponse
import model.api.auth.login.LoginRequest
import model.api.auth.login.LoginResponse
import model.auth.UserSession
import model.db.user.User
import model.db.user.Users
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Route.apiAuthController() {
    val db by inject<Database>()
    val service by inject<AuthService>()

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()

            val user = transaction(db) {
                val user = User.find {
                    (Users.email eq request.email) and
                    (Users.password eq request.password)
                }.with(User::tags).singleOrNull()
                user
            } ?: return@post call.respond(HttpStatusCode.Unauthorized)

            call.sessions.set(service.sessionForUser(user))
            call.respond(
                LoginResponse(
                    token = service.tokenForUser(user),
                    user = user.toCommon(),
                )
            )
        }
        post("/logout") {
            val result = call.sessions.runCatching { clear<UserSession>() }
            call.respond(SuccessResponse(result.isSuccess))
        }
    }
}