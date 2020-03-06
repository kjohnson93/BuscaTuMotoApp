package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import javax.inject.Inject

class DetailRelatedViewModel @Inject constructor() : ViewModel() {
    lateinit var lifeCycleOwner: DetailRelatedFragment
}