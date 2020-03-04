package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.buscatumoto.ui.activities.MotoDetailActivity
import javax.inject.Inject

class MotoDetailViewModel @Inject constructor() : ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity
}