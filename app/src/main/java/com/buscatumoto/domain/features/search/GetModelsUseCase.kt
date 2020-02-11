package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.data.mapper.MotoEntityToUiMapper
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(private val searchRepository: BuscaTuMotoRepository) {

    suspend fun execute(brand: String): LiveData<Result<List<MotoEntity>>> {
        return searchRepository.getModelsByBrand(brand)
    }

    fun setupModels(motosEntity: List<MotoEntity>?): List<String>? {

        val localMotosModel = MotoEntityToUiMapper.map(motosEntity)

        localMotosModel.apply {
            this?.remove("")
            this?.add(0, "-Elegir Modelo-")
        }

        return localMotosModel
    }

}