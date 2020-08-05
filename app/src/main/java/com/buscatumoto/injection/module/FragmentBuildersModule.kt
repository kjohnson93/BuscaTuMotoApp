package com.buscatumoto.injection.module

import com.buscatumoto.ui.fragments.*
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun comtributeSearchDialogFragment(): FilterFormDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeCatalogueFragment(): CatalogueFragment

    @ContributesAndroidInjector
    abstract fun contributeMotoDetailFragment(): MotoDetailHostFragment

    @ContributesAndroidInjector
    abstract fun contributeMotoDetailContentFragment(): DetailContentFragment

    @ContributesAndroidInjector
    abstract fun contributeMotoDetailSpecsFragment(): DetailSpecsFragment

    @ContributesAndroidInjector
    abstract fun contributeMotoDetailRelatedFragment(): DetailRelatedFragment

    @ContributesAndroidInjector
    abstract fun contributeLanguagePickerFragment(): LanguagePickerFragment
}