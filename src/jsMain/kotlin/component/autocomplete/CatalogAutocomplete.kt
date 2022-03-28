package component.autocomplete

import domain.api.ApiService
import domain.koin.get
import model.api.catalog.Catalog
import react.FC
import react.Props

external interface CatalogAutocompleteProps : Props {
    var initial: Set<Catalog>?
    var onChange: (Set<Catalog>) -> Unit
}
val CatalogAutocomplete = FC<CatalogAutocompleteProps> { props ->
    val service = get<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Catalog>> {
        label = "Catalogs"
        initialValues = props.initial

        fetch = { service.getCatalogs() }
    }
}