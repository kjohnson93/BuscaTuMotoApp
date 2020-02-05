package com.buscatumoto.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buscatumoto.data.Result
import com.buscatumoto.data.local.entity.Fields
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.utils.global.Constants
import javax.inject.Inject

class GetFieldsUseCase @Inject constructor(val searchRepository: BuscaTuMotoRepository) {

    var testMutableLiveData = MutableLiveData<Result<Fields>>()

//    lateinit var testLiveData: LiveData<Result<Fields>>

    suspend fun execute(): Result<Fields> {

        var fields : Result<Fields> = Result.success(Fields(-1, "error", emptyList()))

//        searchRepository.getFields()?.let {
//            result ->
//            when (result.status) {
//                Result.Status.SUCCESS -> {
//                    Log.d(Constants.MOTOTAG, "success is ${result}")
//                    result.data?.let {
//                        fields = Result.success(result.data)
//                    }
//                }
//                Result.Status.LOADING -> {
//                    Log.d(Constants.MOTOTAG, "process loading is ${result}")
//                }
//                Result.Status.ERROR -> {
//                    Log.d(Constants.MOTOTAG, "error is ${result.message}")
//                }
//            }
//        }

        searchRepository.getFieldsEmit()?.let {
            liveData: LiveData<Result<Fields>> ->
            when (liveData.value?.status) {
                                Result.Status.SUCCESS -> {
                    Log.d(Constants.MOTOTAG, "success is ${liveData.value}")
                }
                Result.Status.LOADING -> {
                    Log.d(Constants.MOTOTAG, "process loading")
                }
                Result.Status.ERROR -> {
                    Log.d(Constants.MOTOTAG, "error")
                }
                else -> {
                    Log.d(Constants.MOTOTAG, "default error")
                }
            }
        }

        return fields

    }

    suspend fun executeLiveData(): LiveData<Result<Fields>> {
        return searchRepository.getFieldsEmit()
    }
}