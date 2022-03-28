package component.autocomplete

import domain.api.ApiService
import domain.koin.get
import model.api.catalog.Catalog
import react.FC
import react.Props

external interface CatalogAutocompleteProps : Props {
    var value: Set<Catalog>
    var onChange: (Set<Catalog>) -> Unit
}
val CatalogAutocomplete = FC<CatalogAutocompleteProps> { props ->
    val service = get<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Catalog>> {
        label = "Catalogs"
        value = props.value
        onChange = props.onChange

        fetch = { service.catalog.getList() }
        comparator = { t1, t2 -> t1.id == t2.id }
        filter = { t, s -> t.name.contains(s, ignoreCase = true) }
        represent = { t -> t.name }
    }
}