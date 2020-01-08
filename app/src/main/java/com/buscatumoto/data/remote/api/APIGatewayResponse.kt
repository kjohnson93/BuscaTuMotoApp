package com.buscatumoto.data.remote.api

class APIGatewayResponse<T> {

    interface SuccessListener<T> {
        fun onResponse(response: T)
    }

    interface ErrorListener {
        fun onError(errorResponse: String?)
    }

}