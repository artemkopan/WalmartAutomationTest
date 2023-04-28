package com.example.walmart.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo

class CountryDetailsViewModelFactory(
    private val repo: CountryRepo,
    private val dispatchers: DispatcherProvider,
    private val errorFormatter: ErrorFormatter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        @Suppress("UNCHECKED_CAST")
        return CountryDetailsViewModel(
            extras.createSavedStateHandle(),
            repo,
            dispatchers,
            errorFormatter
        ) as T
    }
}