package com.buscatumoto.domain.features.catalogue

import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class LoadCatalogueUseCase @Inject constructor(
    private val buscaTuMotoRepository: BuscaTuMotoRepository
) {

    suspend fun getMotosCatalogue(pageIndex: Int): Result<MotoResponse> {
        val lastParams = buscaTuMotoRepository.getSearchParams()

        return if (lastParams.search != null) {
            buscaTuMotoRepository.getMotosSearchResponse(lastParams.search, pageIndex)
        } else {
            buscaTuMotoRepository.getMotosFilterResponse(
                lastParams.brand,
                lastParams.model,
                lastParams.bikeType,
                lastParams.priceMin?.toIntOrNull(),
                lastParams.priceMax?.toIntOrNull(),
                lastParams.powerMin?.toDoubleOrNull(),
                lastParams.powerMax?.toDoubleOrNull(),
                lastParams.cilMin?.toDoubleOrNull(),
                lastParams.cilMax?.toDoubleOrNull(),
                lastParams.weightMin?.toDoubleOrNull(),
                lastParams.weightMax?.toDoubleOrNull(),
                lastParams.year?.toIntOrNull(),
                lastParams.license,
                pageIndex
            )
        }
    }

    suspend fun saveMoto(moto: MotoEntity?) {

        moto?.let {
            buscaTuMotoRepository.saveMoto(it)
        }
    }

}