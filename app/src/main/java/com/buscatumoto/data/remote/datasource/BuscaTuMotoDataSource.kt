package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.api.BaseDataSource
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService): BaseDataSource() {



    suspend fun getFields() =  getResult { buscaTuMotoService.getFields() }

    suspend fun getMotos(brand: String) = getResult { buscaTuMotoService.getBikesByBrand(brand)}

    suspend fun filter(brand: String? = null,
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
                       license: String? = null
    ) = getResult { buscaTuMotoService.filter(brand, model, bikeType, priceBottom, priceTop, powerBottom, powerTop, displacementBottom, displacementTop, weightBottom, weightTop, year, license) }

    suspend fun search(search: String) = getResult { buscaTuMotoService.search(search) }

}