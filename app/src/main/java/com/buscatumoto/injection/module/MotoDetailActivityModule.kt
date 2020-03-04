package com.buscatumoto.injection.module

import com.buscatumoto.ui.activities.MotoDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MotoDetailActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMotoDetailActivity(): MotoDetailActivity
}