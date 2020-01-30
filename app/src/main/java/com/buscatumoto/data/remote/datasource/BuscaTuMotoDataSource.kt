package com.buscatumoto.data.remote.datasource

import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private val injector: ViewModelComponent = DaggerViewModelComponent.builder().networkModule(
        NetworkModule
    ).build()

    init {
        injector.inject(this)
    }
}