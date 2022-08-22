package model.cms

import model.db.category.Category
import model.db.category.toCommon as toCommonExt
import model.api.category.Category as CommonCategory

object CategoryCmsPattern : DbCmsPattern<Category, CommonCategory>() {
    override val intEntity = Category

    override fun Category.intoEntity(item: model.api.category.Category, isNew: Boolean) {
        name = item.name
    }

    override fun Category.toCommon() = toCommonExt()
}