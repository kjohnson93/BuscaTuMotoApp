package com.buscatumoto.injection.module

import com.buscatumoto.ui.activities.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSearchActivity(): SearchActivity
}