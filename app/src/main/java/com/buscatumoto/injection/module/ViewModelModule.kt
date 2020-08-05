package com.buscatumoto.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buscatumoto.ui.viewmodels.*
import com.buscatumoto.utils.injection.ViewModelFactory
import dagger.*
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

    @Binds
    @IntoMap
    @ViewModelKey(CatalogueViewModel::class)
    abstract fun bindCatalogueViewModel(catalogueViewModel: CatalogueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CatalogueItemViewModel::class)
    abstract fun bindCatalogueItemViewModel(catalogueItemViewModel: CatalogueItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MotoDetailViewModel::class)
    abstract fun bindMotoDetailViewModel(motoDetailViewModel: MotoDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailContentViewModel::class)
    abstract fun bindDetailContentViewModel(detailContentViewModel: DetailContentViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailSpecsViewModel::class)
    abstract fun bindDetailSpecsViewModel(detailSpecsViewModel: DetailSpecsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailRelatedViewModel::class)
    abstract fun bindDetailRelatedViewModel(relatedViewModel: DetailRelatedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailSpecItemViewModel::class)
    abstract fun bindDetailSpecItemViewModel(detailSpecItemViewModel: DetailSpecItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchFormViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguagePickerViewModel::class)
    abstract fun bindIdiomaPickerViewModel(idiomaPickerViewModel: LanguagePickerViewModel): ViewModel

}