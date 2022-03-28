package test

import domain.koin.setupKoinPlatform
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import model.db.author.AuthorAliases
import model.db.author.AuthorContents
import model.db.author.AuthorInfos
import model.db.author.Authors
import model.db.catalog.Catalogs
import model.db.category.Categories
import model.db.periodical.Periodicals
import model.db.publication.*
import model.db.user.UserTagXrefs
import model.db.user.UserTags
import model.db.user.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import javax.sql.DataSource

fun main(args: Array<String>) {
    val koin = KoinApplication.init()
    koin.setupKoinPlatform()

    val db = koin.koin.get<Database>()
    val legacy = koin.koin.get<Database>(named("legacy"))

//    dropAll(db)
//    createSchema(db)
//
//    MigrateAuthors.execute(db, legacy)
//    MigrateCatalogs.execute(db, legacy)
//    MigrateCategories.execute(db, legacy)
//    MigratePeriodicals.execute(db, legacy)
//    MigrateMedia.execute(db, legacy)
//    MigratePeriodicals.execute(db, legacy)
}
private val tables = listOf<Table>(
    Authors, AuthorAliases, AuthorContents, AuthorInfos,
    Catalogs,
    Categories,
    Periodicals,
    Publications,
    PublicationAuthorXrefs,
    PublicationCatalogXrefs,
    PublicationCategoryXrefs,
    PublicationPeriodicalXrefs,
    PublicationAttachments,
    PublicationInventoryNumbers,
    Users, UserTags, UserTagXrefs,
)

private fun createSchema(db: Database) = transaction(db) {
    SchemaUtils.create(*tables.toTypedArray())
}

interface IMigrate {
    fun execute(db: Database, legacy: Database)
}