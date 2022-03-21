package test

import model.db.publication.Publication
import org.ktorm.database.Database
import org.ktorm.database.asIterable

object MigratePublications : IMigrate {
    /**
     * Requires:
     *  - authors
     *  - categories
     *  - catalogs
     *  -
     * */
    override fun execute(db: Database, legacy: Database) {
        val top100 = legacy.useConnection { conn ->
            conn.prepareStatement(PUBLICATIONS_QUERY).executeQuery()
                .asIterable().map {
                    val id = it.getInt("id")
                    val catIds = it.getString("category_ids").split(",")
                    println("pub_id: $id, $catIds")
                }
        }
    }
}

private val PUBLICATIONS_QUERY = """
    SELECT 
    	pub.id, 
        pub.opublikowana = 1 as is_published,
        pub.periodyk = 1 as is_periodical,
        pub.plan = 1 as is_planned,
        pub.licencja = 1 as is_licenced,
        pub.pozycja as position,
        pub.tytul as title,
        pub.podtytul as subtitle,
        pub.tyt_oryg as original_title,
        pub.wspolautor as co_author,
        pub.tlumacz as translator,
        pub.wydanie as edition,
        pub.tom as volume,
        pub.redaktor as redactor,
        pub.kolegium as college,
        pub.ilustrator as illustrator,
        pub.miejsce as place,
        pub.seria as series,
        pub.rok as raw_year,
        pub.wyd_druk as publishing_house,
        pub.wydawca as publisher,
        pub.objetosc as dimensions,
        pub.sygnatura as signature,
        pub.isbn as isbn,
        pub.issn as issn,
        pub.ukd as ukd,
        pub.prawa as copyright,
        pub.kup_na as shop_url,
        pub.zrodlo as `source`,
        pub.dodatek as extra,
        pub.opis as description,
        
        pub.autor as legacy_author,
        pub.lokalizacja as legacy_catalog,
        pub.nr_inw as legacy_inventory_number,
        
        pub.utw_kiedy as created_at,
        pub.red_kiedy as updated_at,
    	GROUP_CONCAT(p2c.id_kat) as category_ids
    FROM amlib_publikacje as pub
    LEFT JOIN amlib_pub2kat as p2c ON
    	pub.id = p2c.id_pub
    GROUP BY pub.id
""".trimIndent()