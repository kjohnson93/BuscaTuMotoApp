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

    /**
     * This methods ask a Dao for the last search command executed on the app. So it can
     * request more pages using the same command and the same parameters.
     */
    private fun loadMotoSearchParams() {
        TODO("THIS WILL BE IMPLEMENTED BY THE USE CASE IN IT'S EXECUTE METHOD.")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}