package com.buscatumoto.data.remote

import android.util.Log
import retrofit2.Response
import com.buscatumoto.data.Result
import com.buscatumoto.utils.global.Constants

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Log.e(Constants.MOTOTAG, "error data source: $message")
        return Result.error("Network call has failed for a following reason: $message")
    }

}

