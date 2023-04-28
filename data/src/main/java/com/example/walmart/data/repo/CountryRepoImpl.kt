package com.example.walmart.data.repo

import com.example.walmart.data.mapper.CountryMapper
import com.example.walmart.data.network.CountryRestService
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.repo.CountryRepo

class CountryRepoImpl(
    private val restService: CountryRestService,
    private val countryMapper: CountryMapper
) : CountryRepo {
    override suspend fun getCountries(): List<Country> {
        return restService.getCountries().map(countryMapper::toModel)
    }

    override suspend fun getCountryDetailsByCode(code: String): Country? {
        // temp solution for get country by code
        return getCountries().firstOrNull { it.code == code }
    }
}