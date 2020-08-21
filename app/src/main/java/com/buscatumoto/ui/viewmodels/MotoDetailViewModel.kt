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
    private val _motoDetailUiMutable = MutableLiveData<MotoDetailUi> ()
    val motoDetailUiLiveData: LiveData<MotoDetailUi> = _motoDetailUiMutable

    fun loadMotoDetail(id: String, fragmentManager: FragmentManager) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntityVal = loadMotoDetailUseCase.executeNoLiveData(id)
            val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(motoEntityVal)
            _motoDetailUiMutable.postValue(motoDetailUi)
            withContext(Dispatchers.Main) {
                bannerLiveData.value = motoDetailUi?.bannerImg
            }
        }
    }
}