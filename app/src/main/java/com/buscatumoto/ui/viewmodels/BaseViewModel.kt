package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.ViewModel
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

//    private val injector: ViewModelComponent = DaggerViewModelComponent
//        .builder().networkModule(NetworkModule).build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies.
     */
    private fun inject() {
//        when(this) {
//            is SearchBikeActivityViewModel -> {
//                injector.inject(this)
//            }
//        }
    }
}