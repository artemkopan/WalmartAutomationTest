package com.example.walmart.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.ext.onError
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo
import com.example.walmart.presentation.countries.CountriesViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repo: CountryRepo,
    private val dispatchers: DispatcherProvider,
    private val errorFormatter: ErrorFormatter
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val effectChannel = Channel<Effect>(
        capacity = Channel.BUFFERED, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effectFlow = effectChannel.receiveAsFlow()

    init {
        loadCountry()
    }

    private fun loadCountry() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(dispatchers.io() + onError(::handleError)) {
            val countryCode = savedStateHandle.get<String>(CountryDetailsArg.COUNTRY_CODE)
            requireNotNull(countryCode)

            val country = repo.getCountryDetailsByCode(code = countryCode)
            _state.update { it.copy(loading = false, country = country) }
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

    fun reloadList() {
        loadCountry()
    }

    fun onBackClicked() {
        viewModelScope.launch {
            effectChannel.send(Effect.OnBack)
        }
    }

    data class State(
        val loading: Boolean = false,
        val country: Country? = null,
        val errorMessage: String? = null
    )

    sealed interface Effect {
        object OnBack : Effect
    }

}