package com.buscatumoto.injection.component

import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.ViewModelModule
import com.buscatumoto.ui.activities.SearchActivity
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import dagger.Component
import javax.inject.Singleton


//TODO Make view model component subcomponent of view component
@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface ViewComponent {



}