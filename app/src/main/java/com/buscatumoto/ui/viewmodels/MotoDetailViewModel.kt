package com.buscatumoto.ui.viewmodels


import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) :
    ViewModel() {

    val bannerLiveData = MutableLiveData<Drawable>()

    init {
        loadMotoDetail()
    }

    fun loadMotoDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntityVal = loadMotoDetailUseCase.getMoto()
            val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(motoEntityVal)
            withContext(Dispatchers.Main) {
                bannerLiveData.value = motoDetailUi?.bannerImg
            }
        }
    }
}