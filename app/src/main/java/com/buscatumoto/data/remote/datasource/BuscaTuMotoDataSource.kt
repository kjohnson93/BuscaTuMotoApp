package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.BaseDataSource
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService): BaseDataSource() {



    suspend fun getFields() = getResult { buscaTuMotoService.getFields() }

    suspend fun getMotos(brand: String) = getResult { buscaTuMotoService.getBikesByBrand(brand)}

}