package route.api

import domain.auth.AuthService
import domain.db.countInt
import domain.db.forIds
import domain.receivePaginatedRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.api.PaginatedResponse
import model.api.publication.AddPublicationRequest
import model.api.publication.PublicationField
import model.cms.*
import model.db.author.Author
import model.db.catalog.Catalog
import model.db.category.Category
import model.db.periodical.Periodical
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
        cmsPattern("catalog", CatalogCmsPattern)
        cmsPattern("author", AuthorCmsPattern)
        cmsPattern("category", CategoryCmsPattern)
        cmsPattern("periodical", PeriodicalCmsPattern) { pattern ->
            get("/list/simple") { call.respond(pattern.getSimpleList()) }
        }

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

        post("publication") {
            val request = call.receive<AddPublicationRequest>()
            val pub = request.publication

            newSuspendedTransaction(db = db) {
                Publication.new {
                    isPublished = pub.isPublished
                    isPeriodical = pub.isPeriodical
                    isPlanned = pub.isPlanned
                    isLicenced = pub.isLicenced
                    position = pub.position

                    title = pub.title
                    subtitle = pub.subtitle
                    originalTitle = pub.originalTitle
                    coAuthor = pub.coAuthor
                    translator = pub.translator

                    edition = pub.edition
                    volume = pub.volume
                    redactor = pub.redactor
                    college = pub.college
                    illustrator = pub.illustrator

                    place = pub.place
                    series = pub.series
                    year = pub.year
                    publishingHouse = pub.publishingHouse
                    publisher = pub.publisher
                    dimensions = pub.dimensions

                    signature = pub.signature
                    isbn = pub.isbn
                    issn = pub.issn
                    ukd = pub.ukd
                    copyright = pub.copyright
                    shopUrl = pub.shopUrl
                    origin = pub.origin

                    extra = pub.extra
                    description = pub.description

                    periodicals = Periodical.forIds(request.periodicalIds)
                    catalogs = Catalog.forIds(request.catalogIds)
                    categories = Category.forIds(request.categoryIds)
                    authors = Author.forIds(request.authorIds)
                }
            }
        }
    }
}