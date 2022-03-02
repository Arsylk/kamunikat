package route.admin.authors

import domain.api.ApiService
import domain.base.EffectViewModel
import domain.base.UiEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import model.Author
import route.admin.authors.AdminAuthorsViewModel.Column.*

class AdminAuthorsViewModel(
    private val apiService: ApiService,
) : EffectViewModel<Effect>() {
    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val orderBy = MutableStateFlow(Id)
    val authors = combine(_authors, orderBy) { list, order ->
        when (order) {
            Id -> list.sortedBy { it.id }
            Name -> list.sortedBy { it.name }
            Content -> list.sortedBy { it.content }
        }
    }


    init {
        loadAuthors()
    }

    fun loadAuthors() {
        errorAware(key = "authors", Dispatchers.Default) {
            _authors.value = apiService.getAuthors()
        }
    }

    enum class Column {
        Id, Name, Content
    }
}

sealed interface Effect : UiEffect