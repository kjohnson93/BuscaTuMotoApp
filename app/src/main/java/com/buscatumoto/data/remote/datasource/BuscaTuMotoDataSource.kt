package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.api.BaseDataSource
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService): BaseDataSource() {



    suspend fun getFields() = getResult { buscaTuMotoService.getFields() }

    suspend fun getMotos(brand: String) = getResult { buscaTuMotoService.getBikesByBrand(brand)}

    suspend fun filter(brand: String? = "",
                       model: String,
                       bikeType: String? = "",
                       priceBottom: Int,
                       priceTop: Int,
                       powerBottom: Double,
                       powerTop: Double,
                       displacementBottom: Double,
                       displacementTop: Double,
                       weightBottom: Double,
                       weightTop: Double,
                       year: Int,
                       license: String
    ) = getResult { buscaTuMotoService.filter(brand, model, bikeType, priceBottom, priceTop, powerBottom, powerTop, displacementBottom, displacementTop, weightBottom, weightTop, year, license) }

}