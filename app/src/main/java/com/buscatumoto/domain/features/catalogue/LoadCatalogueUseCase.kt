package com.buscatumoto.domain.features.catalogue

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.api.resultLiveData
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import timber.log.Timber
import java.lang.NumberFormatException
import javax.inject.Inject

class LoadCatalogueUseCase @Inject constructor(
    private val buscaTuMotoRepository: BuscaTuMotoRepository
) {

    /**
     * Gets the motorcycles values based on filter values and obtains them from a DAO data source.
     * A LiveData is returned because response is ruled by DAO Single Source Of Truth (SSOT)
     * Source is encapsulated in [Result] decorator
     * In the process. An Api network call is made to just update its DAO
     * but response is only retrieved from DAO source.
     */
    suspend fun getMotosFilterSource(pageIndex: Int?): LiveData<Result<PagedListMotoEntity>> {

        val lastParams = buscaTuMotoRepository.getSearchParams()

        Timber.d("params :${lastParams}")

        var priceBottomForm: Int? = null
        var priceTopForm: Int? = null
        var powerBottomForm: Double? = null
        var powerTopForm: Double? = null
        var displacementBottomForm: Double? = null
        var displacementTopForm: Double? = null
        var weightBottomForm: Double? = null
        var weightTopForm: Double? = null
        var yearForm: Int? = null
        var licenseForm: String? = null
        var pageIndexForm: Int? = null

        if (lastParams.search != null) {
            return buscaTuMotoRepository.getMotosSearch(lastParams.search, pageIndex)
        } else {
            try {
                priceBottomForm = lastParams.priceMin?.toInt()
                priceTopForm = lastParams.priceMax?.toInt()
                powerBottomForm = lastParams.powerMin?.toDouble()
                powerTopForm = lastParams.powerMax?.toDouble()
                displacementBottomForm = lastParams.cilMin?.toDouble()
                displacementTopForm = lastParams.cilMax?.toDouble()
                weightBottomForm = lastParams.weightMin?.toDouble()
                weightTopForm = lastParams.weightMax?.toDouble()
                yearForm = lastParams.year?.toInt()
                licenseForm = lastParams.license
//                pageIndexForm = lastParams.pageIndex
            } catch (exception: NumberFormatException) {

            }
            return buscaTuMotoRepository.getMotosFilter(
                lastParams.brand,
                lastParams.model,
                lastParams.bikeType,
                priceBottomForm,
                priceTopForm,
                powerBottomForm,
                powerTopForm,
                displacementBottomForm,
                displacementTopForm,
                weightBottomForm,
                weightTopForm,
                yearForm,
                licenseForm,
                pageIndex
            )
        }
    }

    suspend fun requestCatalogueDatePage(pageIndex: Int): Result<MotoResponse> {
        val lastParams = buscaTuMotoRepository.getSearchParams()

        return buscaTuMotoRepository.fetchCatalogueData(
            lastParams.search,
            pageIndex,
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
            lastParams.license
        )
    }

    suspend fun loadCatalogue(pageIndex: Int): Result<MotoResponse> {

        val lastParams = buscaTuMotoRepository.getSearchParams()

        return buscaTuMotoRepository.fetchCatalogueData(
            lastParams.search,
            pageIndex,
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
            lastParams.license
        )
    }

}