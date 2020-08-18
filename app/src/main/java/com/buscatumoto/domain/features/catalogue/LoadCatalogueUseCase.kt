package com.buscatumoto.domain.features.catalogue

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.api.resultLiveData
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import timber.log.Timber
import java.lang.NumberFormatException
import javax.inject.Inject

class LoadCatalogueUseCase @Inject constructor(
    private val buscaTuMotoRepository: BuscaTuMotoRepository
) {

    /**
     * This methods ask a Dao for the last search command executed on the app. So it can
     * request more pages using the same command and the same parameters.
     */
    suspend fun execute(pageIndex: Int?): LiveData<Result<PagedListMotoEntity>> {

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
            return buscaTuMotoRepository.search(lastParams.search, pageIndex)
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
            return buscaTuMotoRepository.filter(
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

    fun observeCatalogueData(pageIndex: Int) = resultLiveData(
        databaseQuery = { buscaTuMotoRepository.getDaoCatalogue() },
        networkCall = {
            val lastParams = buscaTuMotoRepository.getSearchParams()

            buscaTuMotoRepository.fetchCatalogueData(
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
        },
        saveCallResult = { buscaTuMotoRepository.insertCatalogue(it.motos) }).distinctUntilChanged()

    suspend fun requestCatalogueDatePage(pageIndex: Int) {
        val lastParams = buscaTuMotoRepository.getSearchParams()

        val response = buscaTuMotoRepository.fetchCatalogueData(
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
        response.data?.motos?.let {
            buscaTuMotoRepository.insertCatalogue(it)
        }
    }

}