package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.api.BaseDataSource
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import com.buscatumoto.data.remote.dto.response.MotoResponse
import javax.inject.Inject

/**
 * Class responsible for interacting with Network API Service.
 */
class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService) :
    BaseDataSource() {

    suspend fun getFields() = getResult { buscaTuMotoService.getFields() }

    suspend fun getMotosByBrand(brand: String) = getResult { buscaTuMotoService.getBikesByBrand(brand) }

    suspend fun filter(
        brand: String? = null,
        model: String? = null,
        bikeType: String? = null,
        priceBottom: Int? = null,
        priceTop: Int? = null,
        powerBottom: Double? = null,
        powerTop: Double? = null,
        displacementBottom: Double? = null,
        displacementTop: Double? = null,
        weightBottom: Double? = null,
        weightTop: Double? = null,
        year: Int? = null,
        license: String? = null,
        language: String? = null,
        pageIndex: Int? = null
    ) = getResult {
        buscaTuMotoService.filter(
            brand,
            model,
            bikeType,
            priceBottom,
            priceTop,
            powerBottom,
            powerTop,
            displacementBottom,
            displacementTop,
            weightBottom,
            weightTop,
            year,
            license,
            language,
            pageIndex
        )
    }
    suspend fun search(search: String, language: String? = null,
                       pageIndex: Int?) =
        getResult { buscaTuMotoService.search(search, language, pageIndex) }

    suspend fun searchRelated(id: String, language: String? = null, pageIndex: Int?) =
        getResult { buscaTuMotoService.searchRelated(id, language, pageIndex) }
}