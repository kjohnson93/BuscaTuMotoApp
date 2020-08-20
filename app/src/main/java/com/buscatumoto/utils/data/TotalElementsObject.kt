package com.buscatumoto.utils.data

import androidx.lifecycle.MutableLiveData

object TotalElementsObject {
    var totalElements: Int = 0
    var totalPages: Int = -1
    var resultNumber = -1
    var mutableTest = MutableLiveData<Int>()
}