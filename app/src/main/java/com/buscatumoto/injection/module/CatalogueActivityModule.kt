package com.buscatumoto.injection.module

import com.buscatumoto.ui.activities.CatalogueActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CatalogueActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeCatalogueActivity(): CatalogueActivity
}