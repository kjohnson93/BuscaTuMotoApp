package com.buscatumoto.ui.viewmodels


import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.PagerAdapter
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.activities.MotoDetailActivity
import com.buscatumoto.ui.adapters.DetailViewPagerAdapter
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) :
    ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity

    val bannerLiveData = MutableLiveData<Drawable>()

    private lateinit var detailPagerAdapter: DetailViewPagerAdapter


    fun loadMotoDetail(id: String, fragmentManager: FragmentManager) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntityVal = loadMotoDetailUseCase.executeNoLiveData(id)

            val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(motoEntityVal)

            withContext(Dispatchers.Main) {
                detailPagerAdapter = DetailViewPagerAdapter(motoDetailUi, id, fragmentManager)
                lifeCycleOwner.bindAdapter(detailPagerAdapter)
                bannerLiveData.value = motoDetailUi?.bannerImg
            }
        }
    }
}