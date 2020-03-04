package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.ui.activities.MotoDetailActivity
import timber.log.Timber
import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(val motoDao: MotoDao) : ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity

    var id: String = ""

    public fun loadMotoDetail(id: String) {
        Timber.d("Id on VM: $id")
    }
}