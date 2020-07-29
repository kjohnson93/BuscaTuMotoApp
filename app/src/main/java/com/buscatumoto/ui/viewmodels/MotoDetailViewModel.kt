package com.buscatumoto.ui.viewmodels


import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.viewpager.widget.PagerAdapter
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.activities.MotoDetailActivity
import com.buscatumoto.ui.adapters.DetailViewPagerAdapter
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.ui.fragments.MotoDetailHostFragment
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) :
    ViewModel() {
    lateinit var lifeCycleOwner: LifecycleOwner

    lateinit var tempLifeCyclerOwner: MotoDetailHostFragment

    val bannerLiveData = MutableLiveData<Drawable>()

    private lateinit var detailPagerAdapter: DetailViewPagerAdapter


    fun loadMotoDetail(id: String, fragmentManager: FragmentManager) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntityVal = loadMotoDetailUseCase.executeNoLiveData(id)

            val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(motoEntityVal)

            withContext(Dispatchers.Main) {
                detailPagerAdapter = DetailViewPagerAdapter(motoDetailUi, id, fragmentManager)
                detailPagerAdapter.addFragment(DetailContentFragment(), "Contenido")
                detailPagerAdapter.addFragment(DetailRelatedFragment(), "Relacionados")
                tempLifeCyclerOwner.bindAdapter(detailPagerAdapter)
                bannerLiveData.value = motoDetailUi?.bannerImg
            }
        }
    }
}