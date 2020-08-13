package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val buscaTuMotoRepository: BuscaTuMotoRepository) {

    suspend fun execute(searchString: String, pageIndex: Int?): LiveData<Result<PagedListMotoEntity>> {
        return buscaTuMotoRepository.search(searchString, pageIndex)
    }

}