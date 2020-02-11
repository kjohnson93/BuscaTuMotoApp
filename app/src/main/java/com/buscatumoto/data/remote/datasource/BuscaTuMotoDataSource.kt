package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.api.BaseDataSource
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService): BaseDataSource() {



    suspend fun getFields() = getResult { buscaTuMotoService.getFields() }

    suspend fun getMotos(brand: String) = getResult { buscaTuMotoService.getBikesByBrand(brand)}

}