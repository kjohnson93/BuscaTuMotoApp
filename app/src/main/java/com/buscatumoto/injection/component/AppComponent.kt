package com.buscatumoto.injection.component

import android.app.Application
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.injection.module.CatalogueActivityModule
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.SearchActivityModule
import com.buscatumoto.injection.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class, ViewModelModule::class, SearchActivityModule::class,
        CatalogueActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: BuscaTuMotoApplication)
}