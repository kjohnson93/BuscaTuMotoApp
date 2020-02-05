package com.buscatumoto.utils.ui

import android.R
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.fragments.InfiniteRotationView
import com.buscatumoto.ui.models.BrandRecyclerUiModel

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    //Manualx
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { observableValue: Int? -> view.visibility = observableValue?: View.VISIBLE })
    }

    //Lamda cheatsheet
//    Basic Syntax: Lambda expressions are always wrapped in curly braces:
//
//    val sum = { x: Int, y: Int -> x + y }
//    Example
//    Let's define a function similar to yours in Kotlin:
//
//    fun <T, K> mapToEntry(f1: (T) -> K, f2: (T) -> K) {}
//    The first possibily is straight forward, we simply pass two lambdas as follows:
//
//    mapToEntry<String, Int>({ it.length }, { it.length / 2 })
//    Additionally, it's good to know that if a lambda is the last argument passed to a function, it can be lifted out the parantheses like so:
//
//    mapToEntry<String, Int>({ it.length }) {
//        it.length / 2
//    }
//    The first lambda is passed inside the parantheses, whereas the second isn't.


}


    @BindingAdapter("mutableSpinner")
    fun setSpinner(view: Spinner, spinnerDataObservable: MutableLiveData<List<String>>) {

        val parentActivity: AppCompatActivity? = view.getParentActivity()

        if (parentActivity != null) {
            spinnerDataObservable.value?.let {
                spinnerDataObservable.observe(parentActivity, Observer {
                        spinnerDataObservable : List<String>? ->
                    val brandTypeSpinnerAdapter = ArrayAdapter<String>(parentActivity, R.layout.simple_spinner_item, spinnerDataObservable)
                    view?.adapter = brandTypeSpinnerAdapter
                })
            }
        }
    }

    @BindingAdapter("spinnerData", "firstRow")
    fun setSpinner(view: Spinner, list: List<String>, firstRow: String) {

        val parentActivity: AppCompatActivity? = view.getParentActivity()


        (list as ArrayList<String>).apply {
            this.remove("")
            this.add(0, firstRow) }

        val brandTypeSpinnerAdapter = ArrayAdapter<String>(parentActivity, R.layout.simple_spinner_item, list)
        view?.adapter = brandTypeSpinnerAdapter


    }

    @BindingAdapter("mutableDrawable")
    fun setDrawable(view: ImageView, drawableObservable: MutableLiveData<Drawable>) {

        val parentActivity: AppCompatActivity? = view.getParentActivity()

        if (parentActivity != null) {
                drawableObservable.observe(parentActivity, Observer { drawableObservable: Drawable? -> view.setImageDrawable(drawableObservable) })
        }

    }

    @BindingAdapter("mutableInfiniteViewAdapter")
    fun setRecyclerAdapter(view: InfiniteRotationView, adapter: SearchBrandsRecyclerAdapter) {
        view.setAdapter(adapter)
    }

    @BindingAdapter("mutableBrandDrawable")
    fun setBrandDrawable(view: ImageView, brandObservable: MutableLiveData<BrandRecyclerUiModel>) {

        val parentActivity: AppCompatActivity? = view.getParentActivity()

        if (parentActivity != null) {
            brandObservable.observe(
                parentActivity,
                Observer { drawableObservable: BrandRecyclerUiModel? -> view.setImageDrawable(brandObservable.value?.drawable) })
        }
    }
