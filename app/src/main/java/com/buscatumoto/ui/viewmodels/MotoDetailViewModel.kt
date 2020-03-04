package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.ui.activities.MotoDetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(val motoDao: MotoDao) : ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity

    var id: String = ""

    val priceDescLiveData = MutableLiveData<String>()

    fun loadMotoDetail(id: String) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntity = motoDao.getMotoById(id)

            withContext(Dispatchers.Main) {
//                priceDescLiveData.value = motoEntity?.value?.get(0)?.priceDesc
                motoEntity.observe(lifeCycleOwner, Observer {
                    result ->
                    priceDescLiveData.value = result.priceDesc
                })
            }
        }
    }
}