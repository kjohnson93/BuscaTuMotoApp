package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(private val searchRepository: BuscaTuMotoRepository) {

    suspend fun execute(brand: String): LiveData<Result<FieldsEntity>> {
        return searchRepository.getModelsByBrand(brand)
    }

}