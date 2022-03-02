package domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import domain.db.User
import domain.db.Users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.sessions.*
import model.auth.UserSession
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import java.io.File
import java.util.*

class AuthService(environment: ApplicationEnvironment): KoinComponent {
    private val db by inject<Database>()

    private val sessionsDirectory = environment.config.property("sessions.directory").getString()
    private val secret = environment.config.property("jwt.secret").getString()
    private val issuer = environment.config.property("jwt.issuer").getString()
    private val audience = environment.config.property("jwt.audience").getString()
    private val realm = environment.config.property("jwt.realm").getString()
    private val jwtVerifier: JWTVerifier by lazy {
        JWT.require(Algorithm.HMAC256(secret))
            .withIssuer(issuer)
            .withAudience(audience)
            .build()
    }

    fun tokenForUser(user: User): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .withClaim("id", user.id)
            .withClaim("username", user.username)
            .withClaim("email", user.email)
            .sign(Algorithm.HMAC256(secret))
    }

    fun sessionForUser(user: User): UserSession {
        return UserSession(
            userId = user.id,
        )
    }

    fun setupAuthentication(config: Authentication.Configuration) = with(config) {
        jwt(JwtAuthName) {
            realm = this@AuthService.realm
            verifier(jwtVerifier)
        }

        session<UserSession>(SessionAuthName) {
            validate { session ->
                val user = db.sequenceOf(Users)
                    .firstOrNull { it.id eq session.userId }
                session.takeIf { user != null }
            }
            challenge {
                call.respondRedirect("/")
            }
        }
    }

    fun setupSessions(config: Sessions.Configuration) = with(config) {
        cookie<UserSession>(UserSessionName, directorySessionStorage(File(sessionsDirectory))) {
            cookie.path = "/"
        }
    }

    companion object {
        private const val UserSessionName = "user_session"

        const val JwtAuthName = "auth-jwt"
        const val SessionAuthName = "auth-session"
    }
}


