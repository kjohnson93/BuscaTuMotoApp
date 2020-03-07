package com.buscatumoto.ui.viewmodels


import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.activities.MotoDetailActivity
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) : ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity

    val bannerLiveData = MutableLiveData<Drawable>()

    //TODO make only load banner from use case request.
    fun loadMotoDetail(id: String) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntity = loadMotoDetailUseCase.execute(id)

            val motoEntityVal = loadMotoDetailUseCase.executeNoLiveData(id)

            Timber.d(motoEntityVal.toString())

            withContext(Dispatchers.Main) {
                motoEntity.observe(lifeCycleOwner, Observer {
                        motoResult ->
                    viewModelScope.launch (Dispatchers.IO) {
                        //                        motoResult?.let {  }
                        val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(motoResult)

                        withContext(Dispatchers.Main) {
                             bannerLiveData.value = motoDetailUi?.bannerImg
                        }
                    }
                })
            }
        }
    }



}