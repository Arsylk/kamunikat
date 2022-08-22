package model.cms

import model.db.catalog.Catalog
import model.api.catalog.Catalog as CommonCatalog
import model.db.catalog.toCommon as toCommonExt

object CatalogCmsPattern : DbCmsPattern<Catalog, CommonCatalog>() {
    override val intEntity = Catalog

    override fun Catalog.intoEntity(item: model.api.catalog.Catalog, isNew: Boolean) {
        name = item.name
        letter = item.letter
        hasInventory = item.hasInventory
    }

    override fun Catalog.toCommon(): CommonCatalog = toCommonExt()
}