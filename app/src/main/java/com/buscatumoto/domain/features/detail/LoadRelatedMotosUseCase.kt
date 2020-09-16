package com.buscatumoto.domain.features.detail

import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class LoadRelatedMotosUseCase @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository) {

    suspend fun getMotosRelated(id: String,
                        pageIndex: Int? = null
    ): Result<MotoResponse> {
        return buscaTuMotoRepository.getMotosSearchRelatedResponse(id, pageIndex)
    }
}