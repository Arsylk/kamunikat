package model.cms

import model.db.author.Author
import model.api.author.Author as CommonAuthor
import model.db.author.toCommon as toCommonExt

object AuthorCmsPattern : DbCmsPattern<Author, CommonAuthor>() {
    override val intEntity = Author

    override fun Author.intoEntity(item: CommonAuthor) {
        name = item.name
    }

    override fun Author.toCommon() = toCommonExt()
}