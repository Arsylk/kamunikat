package test

import domain.db.rawExec
import kotlinx.datetime.toKotlinLocalDate
import model.common.Gender
import model.common.Language
import model.db.author.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.format.DateTimeFormatter
import kotlin.math.max

object MigrateAuthors : IMigrate {
    override fun execute(db: Database, legacy: Database) {
        authorsWithInfo(db, legacy)
        authorsExtracted(db, legacy)
    }

    private fun authorsWithInfo(db: Database, legacy: Database) {
        val list = legacy.rawExec(AUTHOR_PAGES_QUERY) { it ->
            LegacyAuthor(
                id = it.getInt("id"),
                name = it.getString("name"),
                email = it.getString("email"),
                rawAlias = it.getString("raw_alias"),
                rawContent = it.getString("raw_content"),
                rawBirthday = it.getString("raw_date"),
                rawGender = it.getString("raw_gender"),
                isAlive = it.getBoolean("is_alive"),
                isBirthdayActive = it.getBoolean("is_birthday_active"),
                isPublished = it.getBoolean("is_published")
            )
        }

        list.forEach { l ->
            val author = transaction(db) { Author.new { name = l.name } }
            l.aliases.map {
                transaction(db) {
                    AuthorAlias.new {
                        this.author = author
                        name = it
                    }
                }
            }

            l.contents.forEach { (lang, text) ->
                transaction(db) {
                    AuthorContent.new {
                        this.author = author
                        language = lang
                        content = text
                    }
                }
            }
            transaction(db) {
                AuthorInfo.new {
                    this.author = author
                    gender = l.gender
                    birthday = l.birthday
                    email = l.email
                    isAlive = l.isAlive
                    isBirthdayActive = l.isBirthdayActive
                    isPublished = l.isPublished
                }
            }
        }
    }

    private fun authorsExtracted(db: Database, legacy: Database) {
        val current = transaction(db) { Author.all().toList() }
        val raw = legacy.rawExec(AUTHOR_RAW_QUERY) { row ->
            row.getString("raw_author").split(",").map(String::trim)
        }.flatten().toSet()

        val new = (raw - current.map { it.name }.toSet())
        transaction(db) {
            new.forEach { name ->
                Authors.insertIgnore {
                    it[this.name] = name
                }
            }
        }
    }

    private fun maxAuthorLen(db: Database): Int {
        val max1 = db.rawExec(AUTHOR_PAGES_QUERY) { row ->
            row.getString("longtitle")
                .split("#")
                .map { it.trim() }
        }.flatten().maxByOrNull { it.length }
        val max2 = db.rawExec("SELECT autor FROM amlib_publikacje GROUP BY autor;") { row ->
            row.getString("autor")
                .split(",")
                .map { it.trim() }
        }.flatten().maxByOrNull { it.length }

        println("$max1, $max2")
        return max(max1?.length ?: 0, max2?.length ?: 0)
    }
}

internal data class LegacyAuthor(
    val id: Int,
    val name: String,
    val email: String?,
    val rawAlias: String,
    val rawContent: String,
    val rawBirthday: String,
    val rawGender: String?,

    val isAlive: Boolean,
    val isBirthdayActive: Boolean,
    val isPublished: Boolean,
) {

    val birthday get() = rawBirthday.runCatching {
        java.time.LocalDate.parse(this, DatePattern).toKotlinLocalDate()
    }.getOrNull()
    val gender get() = Gender.mapFrom(rawGender)
    val aliases get() = rawAlias.split("#").map { it.trim() }.toSet() - name
    val contents: Map<Language, String> get() {
        val langs = LangRegex.findAll(rawContent).map { it.groupValues[1] }.toList()
        if (langs.isEmpty()) return mapOf(Language.Belarusian to rawContent)
        return langs.associate { lang ->
            val content = "###$lang###(.*?)###".toRegex(setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))
                .findAll(rawContent)
                .map { it.groupValues[1] }
                .firstOrNull() ?: ""
            Language.mapFrom(lang) to content.trim()
        }
    }

    companion object {
        private val LangRegex = "###([A-z]{2})###".toRegex()
        private val DatePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
}


private val AUTHOR_PAGES_QUERY = """
    SELECT 
    	site.id, 
        site.pagetitle as name, 
        site.longtitle as raw_alias, 
        site.content as raw_content,
        COALESCE(cv_rawdate.value, '') as raw_date,
        COALESCE(cv_alive.value, 0) as is_alive,
        COALESCE(cv_bactive.value, 0) as is_birthday_active,
        site.published as is_published,
        cv_gender.value as raw_gender,
        IF(
    		replace(cv_email.value, 'email@autora', '') = '',
            NULL,
            replace(cv_email.value, 'email@autora', '')
    	) as email
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
private val AUTHOR_RAW_QUERY = """
    SELECT autor as raw_author 
    FROM amlib_publikacje GROUP BY autor;
""".trimIndent()
