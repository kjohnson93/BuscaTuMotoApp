package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.data.mapper.FieldsEntityToUIMapper
import com.buscatumoto.ui.models.FieldsUI
import javax.inject.Inject

class GetFieldsUseCase @Inject constructor(private val searchRepository: BuscaTuMotoRepository) {


    /**
     * Gets the field values from a DAO data source.
     * A livedata is returned because response is ruled by DAO Single Source Of Truth (SSOT)
     * Source is encapsulated in [Result] decorator
     * In the process. An Api network call is made to just update its DAO
     * but response is only retrieved from DAO source.
     */
    suspend fun getFieldsSource(): LiveData<Result<FieldsEntity>> {
        return searchRepository.getFields()
    }

}