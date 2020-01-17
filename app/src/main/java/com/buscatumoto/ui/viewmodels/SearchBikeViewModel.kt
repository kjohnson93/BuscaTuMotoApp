package com.buscatumoto.ui.viewmodels

import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SearchBikeViewModel: BaseViewModel() {

    private lateinit var subscription: Disposable

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private fun loadFields() {

    }
}