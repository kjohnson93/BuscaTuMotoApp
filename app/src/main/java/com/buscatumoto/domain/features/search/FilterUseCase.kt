package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import java.lang.NumberFormatException
import javax.inject.Inject

class FilterUseCase @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository) {

    /**
     * Gets the motorcycles values based on filter values and obtains them from a DAO data source.
     * A LiveData is returned because response is ruled by DAO Single Source Of Truth (SSOT)
     * Source is encapsulated in [Result] decorator
     * In the process. An Api network call is made to just update its DAO
     * but response is only retrieved from DAO source.
     */
    suspend fun getMotosFilterSource(brand: String? = null,
                        model: String? = null,
                        bikeType: String? = null,
                        priceBottom: String? = null,
                        priceTop: String? = null,
                        powerBottom: String? = null,
                        powerTop: String? = null,
                        displacementBottom: String? = null,
                        displacementTop: String? = null,
                        weightBottom: String? = null,
                        weightTop: String? = null,
                        year: String? = null,
                        license: String? = null,
                        pageIndex: Int? = null): LiveData<Result<PagedListMotoEntity>> {

        var brandForm: String? = null
        var modelForm: String? = null
        var bikeTypeForm: String? = null
        var priceBottomForm: Int? = null
        var priceTopForm: Int? = null
        var powerBottomForm: Double? = null
        var powerTopForm: Double? = null
        var displacementBottomForm: Double? = null
        var displacementTopForm: Double? = null
        var weightBottomForm: Double? = null
        var weightTopForm: Double? = null
        var yearForm: Int? = null

        val context = BuscaTuMotoApplication.getInstance().applicationContext

        if (!brand.equals(context.getString(R.string.elegir_marca))) {
            brandForm = brand
        }

        if (!model.equals(context.getString(R.string.elegir_modelo))) {
            modelForm = model
        }

        if (!bikeType.equals(context.getString(R.string.elegir_tipo))) {
            bikeTypeForm = bikeType
        }

        try {
            priceBottomForm = priceBottom?.toInt()
            priceTopForm = priceTop?.toInt()
            powerBottomForm = powerBottom?.toDouble()
            powerTopForm = powerTop?.toDouble()
            displacementBottomForm = displacementBottom?.toDouble()
            displacementTopForm = displacementTop?.toDouble()
            weightBottomForm = weightBottom?.toDouble()
            weightTopForm = weightTop?.toDouble()
            yearForm = year?.toInt()
        } catch (exception: NumberFormatException) {
        }

        return buscaTuMotoRepository.getMotosFilter(brandForm, modelForm, bikeTypeForm, priceBottomForm, priceTopForm,
            powerBottomForm, powerTopForm, displacementBottomForm, displacementTopForm, weightBottomForm,
            weightTopForm, yearForm, license, pageIndex)
    }

    /**
     * Asks repository for motorcycles based on filter values.
     * Response means that values are being collected directly by an
     * Api network call which is a API Source.
     * @return [MotoResponse]: Data holder for handling response from server.
     * It contains pagination data
     */
    suspend fun getMotosFilterResponse(brand: String? = null,
                                       model: String? = null,
                                       bikeType: String? = null,
                                       priceBottom: String? = null,
                                       priceTop: String? = null,
                                       powerBottom: String? = null,
                                       powerTop: String? = null,
                                       displacementBottom: String? = null,
                                       displacementTop: String? = null,
                                       weightBottom: String? = null,
                                       weightTop: String? = null,
                                       year: String? = null,
                                       license: String? = null,
                                       pageIndex: Int? = null): MotoResponse? {

        var brandForm: String? = null
        var modelForm: String? = null
        var bikeTypeForm: String? = null
        var priceBottomForm: Int? = null
        var priceTopForm: Int? = null
        var powerBottomForm: Double? = null
        var powerTopForm: Double? = null
        var displacementBottomForm: Double? = null
        var displacementTopForm: Double? = null
        var weightBottomForm: Double? = null
        var weightTopForm: Double? = null
        var yearForm: Int? = null

        val context = BuscaTuMotoApplication.getInstance().applicationContext

        if (!brand.equals(context.getString(R.string.elegir_marca))) {
            brandForm = brand
        }

        if (!model.equals(context.getString(R.string.elegir_modelo))) {
            modelForm = model
        }

        if (!bikeType.equals(context.getString(R.string.elegir_tipo))) {
            bikeTypeForm = bikeType
        }

        try {
            priceBottomForm = priceBottom?.toInt()
            priceTopForm = priceTop?.toInt()
            powerBottomForm = powerBottom?.toDouble()
            powerTopForm = powerTop?.toDouble()
            displacementBottomForm = displacementBottom?.toDouble()
            displacementTopForm = displacementTop?.toDouble()
            weightBottomForm = weightBottom?.toDouble()
            weightTopForm = weightTop?.toDouble()
            yearForm = year?.toInt()
        } catch (exception: NumberFormatException) {
        }

        return buscaTuMotoRepository.getMotosFilterResponse(brandForm, modelForm, bikeTypeForm, priceBottomForm, priceTopForm,
            powerBottomForm, powerTopForm, displacementBottomForm, displacementTopForm, weightBottomForm,
            weightTopForm, yearForm, license, pageIndex)
    }

    suspend fun deleteMotoDao(): List<MotoEntity> {
        return buscaTuMotoRepository.deleteMotoDao()
    }

}