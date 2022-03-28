package route.admin.publications

import domain.api.ApiService
import domain.base.EffectViewModel
import domain.datatable.DatatableSource
import route.admin.authors.Effect

class AdminPublicationsViewModel(
    private val apiService: ApiService,
) : EffectViewModel<Effect>() {
    val source = DatatableSource(fetch = apiService::getPublications)
}