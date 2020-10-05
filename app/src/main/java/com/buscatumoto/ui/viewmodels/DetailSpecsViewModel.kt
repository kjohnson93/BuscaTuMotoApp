package com.buscatumoto.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.ui.adapters.DetailSpecsRecyclerAdapter
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import com.buscatumoto.ui.models.MotoDetailUi
import javax.inject.Inject

class DetailSpecsViewModel @Inject constructor(): ViewModel() {
    lateinit var lifeCyclerOwner: DetailSpecsFragment

    val detailSpecsRecyclerAdapter = DetailSpecsRecyclerAdapter()

    private val appContext: Context = BuscaTuMotoApplication.getInstance().applicationContext
    val layoutManager = LinearLayoutManager(
        appContext,
        RecyclerView.VERTICAL,
        false
    )

    fun bind(motoDetailUi: MotoDetailUi) {

    }
}