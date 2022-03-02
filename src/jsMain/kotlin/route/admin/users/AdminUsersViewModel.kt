package route.admin.users

import domain.api.ApiService
import domain.base.EffectViewModel
import domain.datatable.DatatableSource
import route.admin.authors.Effect

class AdminUsersViewModel(
    private val apiService: ApiService,
) : EffectViewModel<Effect>() {
    val source = DatatableSource(25, apiService::getUsers)
}