package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailSpecsViewModel @Inject constructor(private val motoDao: MotoDao): ViewModel() {
    lateinit var lifeCyclerOwner: DetailSpecsFragment

    fun bind(motoDetailUi: MotoDetailUi) {

    }
}