package route.api.auth

import domain.auth.AuthService
import domain.db.Users
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.serialization.SerialName
import model.api.SuccessResponse
import model.api.auth.login.LoginRequest
import model.api.auth.login.LoginResponse
import model.auth.UserSession
import org.koin.ktor.ext.inject
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf


fun Route.apiAuthController() {
    val db by inject<Database>()
    val service by inject<AuthService>()

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()

            val user = db.sequenceOf(Users).firstOrNull {
                (it.email eq request.email) and (it.password eq request.password)
            } ?: return@post call.respond(HttpStatusCode.Unauthorized)

            call.sessions.set(service.sessionForUser(user))
            call.respond(
                LoginResponse(
                    token = service.tokenForUser(user),
                    user = user.toCommonUser(),
                )
            )
        }
        post("/logout") {
            val result = call.sessions.runCatching { clear<UserSession>() }
            call.respond(SuccessResponse(result.isSuccess))
        }
    }
}