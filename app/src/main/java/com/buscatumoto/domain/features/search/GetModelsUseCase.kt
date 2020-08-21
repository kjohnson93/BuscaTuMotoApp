package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(private val searchRepository: BuscaTuMotoRepository) {

    /**
     * Gets the motorcycles values based on a brand values and obtains them from a DAO data source.
     * A LiveData is returned because response is ruled by DAO Single Source Of Truth (SSOT)
     * Source is encapsulated in [Result] decorator
     * In the process. An Api network call is made to just update its DAO
     * but response is only retrieved from DAO source.
     */
    suspend fun getModelsByBrandSource(brand: String): LiveData<Result<FieldsEntity>> {
        return searchRepository.getModelsByBrand(brand)
    }

}