package com.buscatumoto.injection.module

import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailScreenFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeDetailContentFragment(): DetailContentFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailSpecsFragment(): DetailSpecsFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailRelatedFragment(): DetailRelatedFragment

}