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
        url = "jdbc:mysql://db:3306/legacy",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root",
        dialect = MySqlDialect(),
    )

    val pages = db.useConnection { conn ->
        val q = conn.prepareStatement(AUTHOR_PAGES_QUERY).executeQuery()
        q.asIterable().map {
            it.getString("longtitle").split("#")
                .map { it.trim() }
        }
    }
}

private val AUTHOR_PAGES_QUERY = """
    SELECT 
    	site.id, 
        site.published,
        site.pagetitle, 
        site.longtitle, 
        site.content,
        COALESCE(cv_alive.value, 0) as is_alive,
        COALESCE(cv_rawdate.value, '') as raw_date,
        COALESCE(cv_bactive.value, 0) as is_birthday_active,
        cv_gender.value as gender,
        IF(
    		replace(cv_email.value, 'email@autora', '') = '',
            NULL,
            replace(cv_email.value, 'email@autora', '')
    	) as emailmodx_site_content
    FROM 
    	modx_site_content as site
    LEFT JOIN modx_document_groups ON 
    	modx_document_groups.document = site.id 
    LEFT JOIN modx_site_tmplvar_contentvalues as cv_alive ON 
    	cv_alive.contentid = site.id AND cv_alive.tmplvarid = 30
    LEFT JOIN modx_site_tmplvar_contentvalues as cv_rawdate ON 
    	cv_rawdate.contentid = site.id AND cv_rawdate.tmplvarid = 36
    LEFT JOIN modx_site_tmplvar_contentvalues as cv_bactive ON 
    	cv_bactive.contentid = site.id AND cv_bactive.tmplvarid = 37
    LEFT JOIN modx_site_tmplvar_contentvalues as cv_gender ON 
    	cv_gender.contentid = site.id AND cv_gender.tmplvarid = 38
    LEFT JOIN modx_site_tmplvar_contentvalues as cv_email ON 
    	cv_email.contentid = site.id AND cv_email.tmplvarid = 40
    WHERE site.parent = 166
    ORDER BY site.id
""".trimIndent()

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