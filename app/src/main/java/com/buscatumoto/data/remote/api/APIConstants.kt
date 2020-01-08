package com.buscatumoto.data.remote.api

class APIConstants {

    /**
     * URL Endpoints
     */

    companion object {

        const val RESPONSE_OK= "OK"
        const val RESPONSE_ERROR = "KO"

        const val GET_BRANDS_URL = "api/moto/field/brands"

        const val GET_FIELDS_URL = "api/moto/fields"

        const val GET_BIKES_BY_BRAND = "api/moto/field/{brand}"
    }
}