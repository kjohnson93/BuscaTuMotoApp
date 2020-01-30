package com.buscatumoto.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import dagger.*
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Binds
    @IntoMap
    @ViewModelKey(CatalogueViewModel::class)
    abstract fun bindCatalogueViewModel(catalogueViewModel: CatalogueViewModel): ViewModel



}