package com.buscatumoto.injection.component

import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.viewmodels.SearchBikeActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {

    /**
     * Injects required dependencies into the specified SearchBikeViewModel.
     * @param searchBikeActivityViewModel SearchBikeViewModel in which to inject the dependencies
     */
    fun inject(searchBikeActivityViewModel: SearchBikeActivityViewModel)


}