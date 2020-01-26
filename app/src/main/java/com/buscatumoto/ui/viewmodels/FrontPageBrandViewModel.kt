package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import javax.inject.Inject

class FrontPageBrandViewModel: BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private val drawableObservable = MutableLiveData<Drawable>()

    fun getDrawableObservable(): MutableLiveData<Drawable> = drawableObservable

    fun bind(drawable: Drawable) {
        drawableObservable.value = drawable
    }
}