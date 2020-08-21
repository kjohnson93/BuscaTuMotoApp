package com.buscatumoto.domain.features.search

import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val buscaTuMotoRepository: BuscaTuMotoRepository
) {


    suspend fun deleteMotoDao(): List<MotoEntity> {
        return buscaTuMotoRepository.deleteMotoDao()
    }

    suspend fun insertSearch(search: String) {
        buscaTuMotoRepository.insertSearch(search)
    }


}