package com.buscatumoto.utils.ui

import android.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.ui.adapters.CatalogueListAdapter
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

    @BindingAdapter("mutableModelText")
    fun setModelText(view: TextView, textModelObservable: MutableLiveData<String>) {

        val parentActivity: AppCompatActivity? = view.getParentActivity()

        if (parentActivity != null) {
            textModelObservable.observe(parentActivity, Observer { textObservable: String? ->
                view.text = textObservable
            })
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
@BindingAdapter("mutableRefreshing")
fun setRefreshing(view: SwipeRefreshLayout, booleanObservable: MutableLiveData<Boolean>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        booleanObservable.observe(parentActivity, Observer { result ->
            view.isRefreshing = result
        })
    }
}

/**
 * This methods set adapter and layout manager to RecyclerView.
 */
@BindingAdapter("recyclerAdapter")
fun setCatalogueAdapter(view: RecyclerView, adapter: CatalogueListAdapter) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        view.setHasFixedSize(true)
        view.adapter = adapter
    }
}

@BindingAdapter("recyclerLinearLayoutManager")
fun setLayoutManager(view: RecyclerView, linearLayoutManager: LinearLayoutManager) {
        view.layoutManager = linearLayoutManager
}

@BindingAdapter("scrollableListener")
fun setScrollableListener(view: RecyclerView, scrollListener: RecyclerView.OnScrollListener) {
        view.addOnScrollListener(scrollListener)
}
