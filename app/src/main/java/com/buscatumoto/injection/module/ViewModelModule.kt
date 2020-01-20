package com.buscatumoto.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.buscatumoto.ui.viewmodels.SearchBikeActivityViewModel
import com.buscatumoto.ui.viewmodels.SearchBikeFragmentViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import javax.inject.Provider
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
    @ViewModelKey(SearchBikeActivityViewModel::class)
    abstract fun bindsSearchBikeActivityViewModel(searchBikeActivityViewModel: SearchBikeActivityViewModel): ViewModel

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