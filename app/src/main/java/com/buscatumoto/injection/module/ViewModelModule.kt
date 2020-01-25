package com.buscatumoto.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {


    //Creates VieewModelKey annotation
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FrontPageViewModel::class)
    abstract fun bindsFrontPageViewModel(frontPageViewModel: FrontPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchFormViewModel::class)
    abstract fun bindSearchFormViewModel(searchFormViewModel: SearchFormViewModel): ViewModel

//    @Provides
//    fun viewModelFactory(providerMap: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
//        return ViewModelFactory(providerMap)
//    }

//    @Provides
//    @IntoMap
//    @ViewModelKey(SearchBikeActivityViewModel::class)
//    fun provideSearchActivityViewModel(): ViewModel {
//        return SearchBikeActivityViewModel()
//    }
//
//    @Provides
//    @IntoMap
//    @ViewModelKey(SearchBikeFragmentViewModel::class)
//    fun provideSearchBikeFragmentViewModel(): ViewModel {
//        return SearchBikeFragmentViewModel()
//    }
}