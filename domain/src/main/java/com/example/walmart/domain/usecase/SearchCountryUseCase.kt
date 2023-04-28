package com.example.walmart.domain.usecase

import com.example.walmart.domain.model.Country

class SearchCountryUseCase {

    operator fun invoke(list: List<Country>, query: String): List<Country> {
        return if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) || it.region.contains(query, ignoreCase = true)
            }
        }
    }
}