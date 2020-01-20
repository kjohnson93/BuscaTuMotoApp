package com.buscatumoto.injection.component

import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.ViewModelModule
import com.buscatumoto.ui.activities.SearchActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface ViewModelComponent {

    /**
     * Injects required dependencies into the specified SearchBikeViewModel.
     * @param searchBikeActivityViewModel SearchBikeViewModel in which to inject the dependencies
     */
//    fun inject(searchBikeActivityViewModel: SearchBikeActivityViewModel)
    fun inject(searchBikeActivity: SearchActivity)


}