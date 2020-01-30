package com.buscatumoto.injection.component

import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.ViewModelModule
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.ui.viewmodels.FrontPageBrandViewModel
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface ViewModelComponent {

    /**
     * Views
     */
    fun inject(searchFragment: SearchFragment)
    fun inject(filterFormDialogFragment: FilterFormDialogFragment)
    fun inject(catalogueActivity: CatalogueActivity)


    /**
     * ViewModels
     */
    fun inject(frontPageViewModel: FrontPageViewModel)
    fun inject(frontPageBrandViewModel: FrontPageBrandViewModel)
    fun inject(catalogueViewModel: CatalogueViewModel)

    /**
     * Repositories
     */
    fun inject(buscaTuMotoRepository: BuscaTuMotoRepository)


}