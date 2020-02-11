package com.buscatumoto.ui.models


data class FieldsUI(
    val id: Int?,
    val respuesta: String?,
    val brandList: ArrayList<String>? ,
    val bikeTypesList: ArrayList<String>?,
    val priceMinList: ArrayList<String>?,
    val priceMaxList: ArrayList<String>?,
    val powerMinList: ArrayList<String>?,
    val powerMaxList: ArrayList<String>?,
    val cilMinList: ArrayList<String>?,
    val cilMaxList: ArrayList<String>?,
    val weightMinList: ArrayList<String>?,
    val weightMaxList: ArrayList<String>?,
    val yearList: ArrayList<String>?,
    val licenses: ArrayList<String>?
)