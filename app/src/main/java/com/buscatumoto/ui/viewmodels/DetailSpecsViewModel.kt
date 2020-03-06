package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import javax.inject.Inject

class DetailSpecsViewModel @Inject constructor(): ViewModel() {
    lateinit var lifeCyclerOwner: DetailSpecsFragment
}