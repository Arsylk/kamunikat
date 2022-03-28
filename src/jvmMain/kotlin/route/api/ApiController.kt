package route.api

import domain.auth.AuthService
import domain.db.*
import domain.receivePaginatedRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import model.api.PaginatedResponse
import model.api.SuccessResponse
import model.api.user.AddUserRequest
import model.common.sortedBy
import model.db.user.User
import model.db.user.UserTag
import model.db.user.UserTags
import model.db.user.Users
import model.user.UserField
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.selectBatched
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import route.api.auth.apiAuthController
import model.user.UserTag as CommonUserTag

fun Routing.apiController() {
    route("/api") {
        apiAuthController()

        crudPublications()
        crudUsers()
        crudUserTags()
    }
}

private fun Route.crudUsers() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("/users") {
            val params = call.receivePaginatedRequest<UserField>()

            val list = transaction(db) {
                val query = Users.selectAll()
                    .limit(params.perPage, params.offset)
                    .orderBy(Users.id) // TODO order sorting
                User.wrapRows(query).map { it.toCommon() } to User.countInt()
            }

            call.respond(
                PaginatedResponse(
                    page = params.page,
                    perPage = params.perPage,
                    order = params.order,
                    orderSelect = params.orderSelect,
                    fullSize = list.second,
                    items = list.first,
                )
            )
        }
        post("/user") {
            val req = call.receive<AddUserRequest>()
            transaction(db) {
                User.new {
                    username = req.username
                    email = req.email
                    password = req.password
                    tags = UserTag.find { UserTags.id inList req.tagIds }
                }
            }
            call.respond(SuccessResponse())
        }
    }
}

private fun Route.crudUserTags() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("/user-tags") {
            val list = transaction(db) { UserTag.all().map { it.toCommon() } }
            call.respond(list)
        }
        post("/user-tag") {
            val tag = call.receive<CommonUserTag>()
            transaction(db) {
                UserTag.new {
                    name = tag.name
                }
            }
            call.respond(SuccessResponse())
        }
    }
}