package com.example.walmart.presentation.di

import com.example.walmart.domain.di.add
import com.example.walmart.domain.di.get
import com.example.walmart.domain.di.module
import com.example.walmart.presentation.countries.CountriesViewModel
import com.example.walmart.presentation.countries.CountriesViewModelFactory
import com.example.walmart.presentation.details.CountryDetailsViewModel
import com.example.walmart.presentation.details.CountryDetailsViewModelFactory

val presentationModule = module {
    add { CountriesViewModelFactory(repo = get(), dispatchers = get(), errorFormatter = get()) }
    add {
        CountryDetailsViewModelFactory(
            repo = get(),
            dispatchers = get(),
            errorFormatter = get()
        )
    }
}