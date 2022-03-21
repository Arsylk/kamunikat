package test

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.support.mysql.MySqlDialect


fun main(args: Array<String>) {
    KoinApplication.init()
    val db = Database.connect(
        url = "jdbc:mysql://db:3306/database",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root",
        dialect = MySqlDialect(),
    )

    val legacy = Database.connect(
        url = "jdbc:mysql://db:3306/legacy",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root",
        dialect = MySqlDialect(),
    )

    MigratePublications.execute(db, legacy)
}

interface IMigrate {
    fun execute(db: Database, legacy: Database)
}


private val PUB_ATTACHMENTS_QUERY = """
    WITH dataset AS (
    	SELECT mi.* FROM media as m
    	JOIN amlib_publikacje as pub ON pub.id = m.pub_id 
    	JOIN media as mi ON m.id = mi.rodzic
        UNION
        SELECT m.* FROM media as m
    	JOIN amlib_publikacje as pub ON pub.id = m.pub_id 
        WHERE m.rodzic = -1
    )
    SELECT 
        dataset.id as id,
        if(dataset.rodzic <> -1, dataset.rodzic, null) as parent_id,
    	dataset.pub_id as publication_id,
        dataset.tytul as title,
        if(dataset.file_typ <> '0', concat(dataset.pub_id, '-', dataset.chap_id, '.', dataset.file_typ), null) as filename,
        dataset.poz as ordered
    FROM dataset;
""".trimIndent()