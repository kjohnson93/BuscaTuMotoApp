package com.buscatumoto.ui.viewmodels

import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService
}