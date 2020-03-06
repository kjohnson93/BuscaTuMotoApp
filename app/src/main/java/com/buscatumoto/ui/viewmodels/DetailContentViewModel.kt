package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.buscatumoto.ui.fragments.DetailContentFragment
import javax.inject.Inject

class DetailContentViewModel @Inject constructor(): ViewModel() {


    lateinit var lifeCycleOwner: DetailContentFragment
}