package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DetailSpecItemViewModel @Inject constructor(): ViewModel() {

    var mutableText = MutableLiveData<String> ()

    fun bind(text: String) {
        mutableText.value = text
    }
}