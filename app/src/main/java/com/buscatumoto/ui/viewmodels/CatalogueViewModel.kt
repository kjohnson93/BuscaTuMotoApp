package com.buscatumoto.ui.viewmodels

import com.buscatumoto.data.remote.api.BuscaTuMotoService
import javax.inject.Inject

class CatalogueViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService
}