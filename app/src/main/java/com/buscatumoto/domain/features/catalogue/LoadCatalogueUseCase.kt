package com.buscatumoto.domain.features.catalogue

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class LoadCatalogueUseCase @Inject constructor(private val buscaTuMotoRepository: BuscaTuMotoRepository
) {

    suspend fun execute(): LiveData<Result<List<MotoEntity>>> {

       return buscaTuMotoRepository.getMotos()

    }


}