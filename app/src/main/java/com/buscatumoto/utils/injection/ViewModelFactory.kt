package com.buscatumoto.utils.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory
    @Inject constructor(
        private val mProviderMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    )
    : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return mProviderMap.get(modelClass)?.get() as T
        val creator = mProviderMap[modelClass] ?:
                mProviderMap.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key)}?.value
        ?: throw IllegalArgumentException("unknown model class" + modelClass)
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }


    }
}