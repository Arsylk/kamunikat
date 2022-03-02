package route.api

import domain.auth.AuthService
import domain.db.Users
import domain.receivePaginatedRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import model.Author
import model.api.PaginatedResponse
import model.common.sortedBy
import model.user.UserField
import org.koin.ktor.ext.inject
import org.ktorm.database.Database
import org.ktorm.entity.*
import route.api.auth.apiAuthController

private val authors = mutableListOf(
    Author(1, "name a", "text 23"),
    Author(2, "name b", "super longer text"),
    Author(3, "name c", "no content"),
)

fun Routing.apiController() {
    route("/api") {
        apiAuthController()

        getAuthors()
        getAuthor()
        putAuthor()

        curdUsers()
    }
}

private fun Route.getAuthors() {
    get("/authors") {
        call.respond(authors)
    }
}

private fun Route.getAuthor() {
    @Location("/author/{id}") data class AuthorParams(val id: Int)
    get<AuthorParams> { params ->
        call.respond(authors.first { it.id == params.id })
    }
}

private fun Route.putAuthor() {
    put("/author") {
        val author = call.receive<Author>()
        authors += author
        call.respond(HttpStatusCode.Accepted)
    }
}


private fun Route.curdUsers() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("/users") {
            val params = call.receivePaginatedRequest<UserField>()

            val list = db.sequenceOf(Users)
                .sortedBy(params.orderSelect, params.order)
                .drop(params.page * params.perPage)
                .take(params.perPage)
                .map { it.toCommonUser() }
            val size = db.sequenceOf(Users).count()

            call.respond(
                PaginatedResponse(
                    page = params.page,
                    perPage = params.perPage,
                    order = params.order,
                    orderSelect = params.orderSelect,
                    fullSize = size,
                    list = list,
                )
            )
        }
    }
}