@file:OptIn(FlowPreview::class)

package com.example.walmart.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.ext.onError
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo
import com.example.walmart.domain.usecase.SearchCountryUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CountriesViewModel(
    private val repo: CountryRepo,
    private val dispatchers: DispatcherProvider,
    private val errorFormatter: ErrorFormatter
) : ViewModel() {

    private val searchCountryUseCase = SearchCountryUseCase()

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val searchFlow = MutableSharedFlow<String>()
    private val effectChannel = Channel<Effect>(
        capacity = Channel.BUFFERED, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effectFlow = effectChannel.receiveAsFlow()

    init {
        loadItems()
        subscribeOnSearchFlow()
    }

    private fun subscribeOnSearchFlow() {
        searchFlow
            .debounce(200.toDuration(DurationUnit.MILLISECONDS))
            .onEach { query ->
                _state.update { it.copy(items = searchCountryUseCase(it.originalItems, query)) }
            }
            .launchIn(viewModelScope)
    }

    fun reloadList() {
        loadItems()
    }

    fun search(query: String) {
        viewModelScope.launch { searchFlow.emit(query) }
    }

    fun onItemClick(item: Country) {
        viewModelScope.launch { effectChannel.send(Effect.OpenDetails(item.code)) }
    }

    private fun loadItems() {
        _state.update { it.copy(loading = true, errorMessage = null) }
        viewModelScope.launch(onError(::handleError) + dispatchers.io()) {
            val items = repo.getCountries()
            _state.update {
                it.copy(
                    loading = false,
                    items = searchCountryUseCase(items, it.query),
                    originalItems = items,
                    errorMessage = null
                )
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        _state.update {
            it.copy(
                loading = false,
                errorMessage = errorFormatter.getDisplayErrorMessage(throwable)
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val query: String = "",
        val originalItems: List<Country> = emptyList(),
        val items: List<Country> = emptyList()
    )

    sealed interface Effect {
        data class OpenDetails(val countryCode: String) : Effect
    }
}