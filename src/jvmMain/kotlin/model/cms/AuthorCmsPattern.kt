package model.cms

import model.db.author.Author
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.with
import model.api.author.Author as CommonAuthor
import model.db.author.toCommon as toCommonExt

object AuthorCmsPattern : DbCmsPattern<Author, CommonAuthor>() {
    override val intEntity = Author

    override fun Author.intoEntity(item: CommonAuthor) {
        name = item.name
    }

    override fun Author.toCommon() = toCommonExt()

    override fun IntEntityClass<Author>.listQuery() =
        all().with(Author::aliases, Author::info)
}