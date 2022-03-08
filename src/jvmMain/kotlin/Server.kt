import domain.auth.AuthService
import domain.koin.plusAssign
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import domain.koin.setupKoin
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.sessions.*
import model.api.PaginatedRequest
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.ktorm.database.Database
import org.slf4j.event.Level
import route.admin.adminController
import route.api.apiController
import route.base.baseController
import javax.naming.Context
import javax.naming.InitialContext
import javax.sql.DataSource


fun main(args: Array<String>): Unit = io.ktor.server.tomcat.EngineMain.main(args)

fun Application.main(testing: Boolean = false) {
    install(Koin) {
        setupKoin()
        this += module { single { environment } }
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) { json(get()) }
    install(Locations)
    install(StatusPages) {
        exception<Throwable> { t ->
            call.respond(
                TextContent(t.stackTraceToString(), ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            )
            throw t
        }
    }
    install(Authentication, get<AuthService>()::setupAuthentication)
    install(Sessions, get<AuthService>()::setupSessions)


    routing {
        static("static") {
            static("js") {
                resource("kamunikat.js")
                resource("kamunikat.js.map")
            }
        }

        baseController()
        adminController()
        apiController()
    }
}
