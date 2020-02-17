package com.buscatumoto.domain.features.search

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import java.lang.NumberFormatException
import javax.inject.Inject

class FilterUseCase @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository) {

    suspend fun execute(brand: String? = null,
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
                        license: String? = null): LiveData<Result<List<MotoEntity>>> {

        var priceBottomForm: Int? = null
        var priceTopForm: Int? = null
        var powerBottomForm: Double? = null
        var powerTopForm: Double? = null
        var displacementBottomForm: Double? = null
        var displacementTopForm: Double? = null
        var weightBottomForm: Double? = null
        var weightTopForm: Double? = null
        var yearForm: Int? = null

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

        return buscaTuMotoRepository.filter(brand, model, bikeType, priceBottomForm, priceTopForm,
            powerBottomForm, powerTopForm, displacementBottomForm, displacementTopForm, weightBottomForm,
            weightTopForm, yearForm, license)
    }
}