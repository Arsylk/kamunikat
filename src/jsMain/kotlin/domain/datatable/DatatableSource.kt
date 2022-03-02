package domain.datatable

import component.datatable.DatatableItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import model.api.PaginatedResponse
import model.common.Order
import model.common.SortSelectable
import model.datatable.DatatableList

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DatatableSource<T: Enum<T>, R: SortSelectable<T>>(perPage: Int) {
    private val _state = MutableStateFlow<State>(State.Uninitialized)
    private val _request = MutableStateFlow<DatasourceRequest<T>>(
        DatasourceRequest(page = 0, perPage = perPage, order = null, orderSelect = null)
    )

    private val _items = MutableStateFlow<List<R>>(emptyList())
    val state by lazy(_state::asStateFlow)
    val current = _request.transformLatest { request ->
            _state.value = State.Loading
            val result = kotlin.runCatching {
                fetch(
                    page = request.page,
                    perPage = request.perPage,
                    order = request.order,
                    orderSelect = request.orderSelect,
                )
            }
            currentCoroutineContext().ensureActive()
            result.apply {
                onSuccess {
                    emit(it)
                    _state.value = State.Success
                }
                onFailure {
                    _state.value = State.Error(it)
                }
            }
        }
        .map { resp ->
            DatatableList(
                page = resp.page,
                perPage = resp.perPage,
                order = resp.order,
                orderSelect = resp.orderSelect,
                items = resp.list
            )
        }

    fun setPage(page: Int) { _request.update { it.copy(page = page) } }

    fun setPerPage(perPage: Int) { _request.update { it.copy(perPage = perPage) } }

    fun setOrder(order: Order?) { _request.update { it.copy(order = order) } }

    fun setOrderSelect(orderSelect: T?) { _request.update { it.copy(orderSelect = orderSelect) } }


    /* @Throws(Throwable::class) */
    protected abstract suspend fun fetch(
        page: Int,
        perPage: Int,
        order: Order?,
        orderSelect: T?,
    ): PaginatedResponse<T, R>


    private data class DatasourceRequest<T: Enum<T>>(
        val page: Int,
        val perPage: Int,
        val order: Order?,
        val orderSelect: T?,
    )

    sealed interface State {
        object Uninitialized : State
        object Success : State
        object Loading : State
        data class Error(val throwable: Throwable) : State
    }
}

fun <T: Enum<T>, R: SortSelectable<T>> DatatableSource(
    perPage: Int,
    fetch: suspend (Int, Int, Order?, T?) -> PaginatedResponse<T, R>,
) = object : DatatableSource<T, R>(perPage) {
    override suspend fun fetch(
        page: Int,
        perPage: Int,
        order: Order?,
        orderSelect: T?,
    ): PaginatedResponse<T, R> = fetch(page, perPage, order, orderSelect)
}
