package com.buscatumoto.domain

import androidx.lifecycle.LiveData
import com.buscatumoto.data.Result
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.dto.response.MotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.utils.mapper.FieldsEntityToUIMapper
import com.buscatumoto.utils.mapper.ui.FieldsUI
import com.buscatumoto.utils.mapper.ui.MotoUI
import javax.inject.Inject

class GetFieldsUseCase @Inject constructor(val searchRepository: BuscaTuMotoRepository) {


    suspend fun executeLiveData(): LiveData<Result<FieldsEntity>> {
        return searchRepository.getFieldsEmit()
    }

    fun addFirstRowLabel(brandList: List<String>?): ArrayList<String> {
        val result = brandList as ArrayList
        result.add(0, "-Marca-")

        return result
    }

    //This method modifies data coming from API because we need to make adjustments to list data.
    fun setupFieldsData(data: FieldsEntity?): FieldsUI {

        val fieldsUI = FieldsEntityToUIMapper.map(data)

        fieldsUI.brandList.apply {
            this?.remove("")
            this?.add(0,"-Marca-")
        }

        fieldsUI.bikeTypesList.apply {
            this?.remove("")
            this?.add(0, "-Tipo de moto-")
        }

        fieldsUI.priceMinList.apply {
            this?.remove("")
            this?.add(0, "-Precio desde-")
        }

        fieldsUI.priceMaxList.apply {
            this?.remove("")
            this?.add(0, "-Precio hasta-")
        }

        fieldsUI.powerMinList.apply {
            this?.remove("")
            this?.add(0, "-Potencia desde-")
        }

        fieldsUI.powerMaxList.apply {
            this?.remove("")
            this?.add(0, "-Potencia hasta-")
        }

        fieldsUI.cilMinList.apply {
            this?.remove("")
            this?.add(0, "-Cilindrada desde-")
        }

        fieldsUI.cilMaxList.apply {
            this?.remove("")
            this?.add(0, "-Cilindrada hasta-")
        }

        fieldsUI.weightMinList.apply {
            this?.remove("")
            this?.add(0, "-Peso desde-")
        }

        fieldsUI.weightMaxList.apply {
            this?.remove("")
            this?.add(0, "-Peso hasta-")
        }

        fieldsUI?.yearList.apply {
            this?.remove("")
            this?.add(0, "-Año-")
        }

        fieldsUI.licenses.apply {
            this?.remove("")
            this?.add(0, "-Permiso-")
        }

        return fieldsUI
    }


}