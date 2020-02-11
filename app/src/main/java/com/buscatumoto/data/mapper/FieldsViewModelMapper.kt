package com.buscatumoto.data.mapper

import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.ui.models.FieldsUI


object FieldsEntityToUIMapper: BaseMapper<FieldsEntity, FieldsUI> {

    override fun map(type: FieldsEntity?): FieldsUI {
        return FieldsUI(
            id = type?.id,
            respuesta = type?.respuesta,
            brandList = type?.brandList as ArrayList<String>?,
            bikeTypesList = type?.bikeTypesList as ArrayList<String>?,
            priceMinList = type?.priceMinList?.map { it.toString() } as? ArrayList<String>?,
            priceMaxList = type?.priceMaxList?.map { it.toString() } as? ArrayList<String>?,
            powerMinList = type?.powerMinList?.map { it.toString() } as? ArrayList<String>?,
            powerMaxList = type?.powerMaxList?.map { it.toString() } as? ArrayList<String>?,
            cilMinList = type?.cilMinList?.map { it.toString() } as? ArrayList<String>?,
            cilMaxList = type?.cilMaxList?.map { it.toString() } as? ArrayList<String>?,
            weightMinList = type?.weightMinList?.map { it.toString() } as? ArrayList<String>?,
            weightMaxList = type?.weightMaxList?.map { it.toString() } as? ArrayList<String>?,
            yearList = type?.yearList?.map { it.toString() } as? ArrayList<String>?,
            licenses = type?.licenses as? ArrayList<String>?
        )
    }
}




