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
import model.user.UserField
import org.koin.ktor.ext.inject
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import route.api.auth.apiAuthController
import model.user.UserTag as CommonUserTag

fun Routing.apiController() {
    route("/api") {
        apiAuthController()

        crudUsers()
        crudUserTags()
    }
}

private fun Route.crudUsers() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("/users") {
            val params = call.receivePaginatedRequest<UserField>()

            val list = db.sequenceOf(Users)
                .sortedBy(params.orderSelect, params.order)
                .drop(params.page * params.perPage)
                .take(params.perPage)
                .map { it.toCommon() }
            val size = db.sequenceOf(Users).count()

            call.respond(
                PaginatedResponse(
                    page = params.page,
                    perPage = params.perPage,
                    order = params.order,
                    orderSelect = params.orderSelect,
                    fullSize = size,
                    items = list,
                )
            )
        }
        post("/user") {
            val req = call.receive<AddUserRequest>()
            val newId = db.sequenceOf(Users).add(
                User {
                    username = req.username
                    email = req.email
                    password = req.password
                }
            )
            val newUser = db.sequenceOf(Users).find { it.id eq newId }!!
            req.tagIds.map { tagId ->
                UserTagXref {
                    tag = db.sequenceOf(UserTags).find { it.id eq tagId }!!
                    user = newUser
                }
            }.forEach { xRef ->
                db.sequenceOf(UserTagXrefs).add(xRef)
            }
            call.respond(SuccessResponse())
        }
    }
}

private fun Route.crudUserTags() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("/user-tags") {
            val list = db.sequenceOf(UserTags).map { it.toCommon() }
            call.respond(list)
        }
        post("/user-tag") {
            val tag = call.receive<CommonUserTag>()
            db.sequenceOf(UserTags).add(
                UserTag {
                    name = tag.name
                }
            )
            call.respond(SuccessResponse())
        }
    }
}