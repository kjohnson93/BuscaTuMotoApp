package com.buscatumoto.injection.component

import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.ViewModelModule
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelServiceComponent {

    fun inject(searchFormViewModel: SearchFormViewModel)
}