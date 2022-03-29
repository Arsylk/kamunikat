package model.cms

import domain.cms.CmsPattern
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

inline fun <reified Common: Any> Route.cmsPattern(
    baseUrl: String,
    pattern: CmsPattern<Common>
) {
    route(baseUrl) {
        get("/list") {
            val list = pattern.getList()
            call.respond(list)
        }
        post {
            val item = call.receiveOrNull<Common>()
                ?: return@post call.respond(HttpStatusCode.BadRequest, "invalid body")
            val newItem = pattern.add(item)
            call.respond(newItem)
        }
        route("/{id}") {
            get {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "invalid id")
                val item = pattern.get(id)
                call.respond(item)
            }
            put {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "invalid id")
                val item = call.receiveOrNull<Common>()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "invalid body")
                val response = pattern.update(id, item)
                call.respond(response)
            }
            delete {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "invalid id")
                val response = pattern.delete(id)
                call.respond(response)
            }
        }
    }
}