package com.buscatumoto.domain

import androidx.lifecycle.LiveData
import com.buscatumoto.data.Result
import com.buscatumoto.data.local.entity.Fields
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import javax.inject.Inject

class GetFieldsUseCase @Inject constructor(val searchRepository: BuscaTuMotoRepository) {


    suspend fun executeLiveData(): LiveData<Result<Fields>> {
        return searchRepository.getFieldsEmit()
    }

    fun addFirstRowLabel(brandList: List<String>?): ArrayList<String> {
        val result = brandList as ArrayList
        result.add(0, "-Marca-")

        return result
    }

    //This method modifies data coming from API because we need to make adjustments to list data.
    fun setupFieldsData(data: Fields?): Fields {

//        var result = Fields()

        val localBrands = data?.brandList as? ArrayList
        localBrands?.remove("")
        localBrands?.add(0,"-Marca-")

        val localBikeTypes = data?.bikeTypesList as? ArrayList
        localBikeTypes?.remove("")
        localBikeTypes?.add(0, "-Tipo de moto-")

        return Fields(data?.id, data?.respuesta, localBrands, localBikeTypes)


//                brands.value =
//            (fieldsResponse?.brandList as ArrayList<String>).apply {
//                this.remove("")
//                this.add(0, "-Marca-") }
//        models.value = ArrayList<String>().apply {
//            this.remove("")
//            this.add(0, "-Elegir Marca-") }
//        bikeTypes.value = (fieldsResponse?.bikeTypesList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Tipo de moto-"
//            )
//        }
//        priceMinList.value = (fieldsResponse?.priceMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Precio desde-"
//            )
//        }
//        priceMaxList.value = (fieldsResponse?.priceMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Precio hasta-"
//            )
//        }
//        powerMinList.value = (fieldsResponse?.powerMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Potencia desde-"
//            )
//        }
//        powerMaxList.value = (fieldsResponse?.powerMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Potencia hasta-"
//            )
//        }
//        cilMinList.value = (fieldsResponse?.cilMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Cilindrada desde-"
//            )
//        }
//        cilMaxList.value = (fieldsResponse?.cilMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Cilindrada hasta-"
//            )
//        }
//        weightMinList.value = (fieldsResponse?.weightMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Peso desde-"
//            )
//        }
//        weightMaxList.value = (fieldsResponse?.weightMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Peso hasta-"
//            )
//        }
//        yearList.value =
//            (fieldsResponse?.yearList as ArrayList<String>).apply { this.add(0, "-Año-") }
//        licenses.value =
//            (fieldsResponse?.licenses as ArrayList<String>).apply { this.add(0, "-Permiso-") }

    }
}