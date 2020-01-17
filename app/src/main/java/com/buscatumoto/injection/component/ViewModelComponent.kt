package com.buscatumoto.injection.component

import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.viewmodels.SearchBikeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {

    /**
     * Injects required dependencies into the specified SearchBikeViewModel.
     * @param searchBikeViewModel SearchBikeViewModel in which to inject the dependencies
     */
    fun inject(searchBikeViewModel: SearchBikeViewModel)


}