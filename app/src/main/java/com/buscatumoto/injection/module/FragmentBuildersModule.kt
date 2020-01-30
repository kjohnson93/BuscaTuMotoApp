package com.buscatumoto.injection.module

import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun comtributeSearchDialogFragment(): FilterFormDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}