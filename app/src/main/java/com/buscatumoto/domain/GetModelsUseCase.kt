package com.buscatumoto.domain

import androidx.lifecycle.LiveData
import com.buscatumoto.data.Result
import com.buscatumoto.data.remote.dto.response.MotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.utils.mapper.MotoEntityToUiMapper
import com.buscatumoto.utils.mapper.ui.MotoUI
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(val searchRepository: BuscaTuMotoRepository) {

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