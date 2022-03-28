package route.api

import domain.auth.AuthService
import domain.db.countInt
import domain.receivePaginatedRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import model.api.PaginatedResponse
import model.api.PublicationField
import model.db.publication.Publication
import model.db.publication.Publications
import model.db.publication.toCommon
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.ktor.ext.inject

fun Route.crudPublications() {
    val db by inject<Database>()
    authenticate(AuthService.JwtAuthName) {
        get("publications") {
            val params = call.receivePaginatedRequest<PublicationField>()
            val list = newSuspendedTransaction(db = db) {
                Publication.all().limit(params.perPage, params.offset)
                    .orderBy(Publications.id to SortOrder.ASC)
                    .map(Publication::toCommon) to Publication.countInt()
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
    }
}