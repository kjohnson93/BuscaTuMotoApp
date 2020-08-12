package com.buscatumoto.utils.ui

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.ui.adapters.CatalogueListAdapter
import com.buscatumoto.ui.adapters.DetailSpecsRecyclerAdapter
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.fragments.InfiniteRotationView
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.utils.global.*
import timber.log.Timber

@BindingAdapter("recyclerAdapter")
fun setRecyclerAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { observableValue: Int? -> view.visibility = observableValue?: View.VISIBLE })
    }
}

@BindingAdapter("mutableExpand")
fun setMutableExpand(view: View, visibility: MutableLiveData<Boolean>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    val context = BuscaTuMotoApplication.getInstance().applicationContext

    val fadeIn = fadeInAnimation(context)
    val fadeOut = fadeOutAnimation(context)

    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            if (it) {
                view.apply {
                    // Set the content view to 0% opacity but visible, so that it is visible
                    // (but fully transparent) during the animation.
                    alpha = ALPHA_OPACITY_TRANSPARENT
                    view.visibility = View.VISIBLE

                    // Animate the content view to 100% opacity, and clear any animation
                    // listener set on the view.
                    animate()
                        .alpha(ALPHA_OPACITY_OPAQUE)
                        .setDuration(FADE_IN_ANIMATION_DURATION)
                        .setListener(null)
                }
            } else {
                view.apply {
                    // Set the content view to 0% opacity but visible, so that it is visible
                    // (but fully transparent) during the animation.
                    alpha = ALPHA_OPACITY_OPAQUE

                    // Animate the content view to 100% opacity, and clear any animation
                    // listener set on the view.
                    animate()
                        .alpha(ALPHA_OPACITY_TRANSPARENT)
                        .setDuration(FADE_OUT_ANIMATION_DURATION)
                        .setListener(object: AnimatorListenerAdapter() {
                            //Usa un onAnimationEnd() en un Animator.AnimatorListener
                            //para establecer la visibilidad de la vista que se desvanecía en GONE
                            //https://developer.android.com/training/animation/reveal-or-hide-view#Crossfade
                            override fun onAnimationEnd(animation: Animator?) {
                                view.visibility = View.GONE
                            }
                        })
                }
            }
        })
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

    @BindingAdapter("mutableText")
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

@BindingAdapter("mutableBackground")
fun setBackground(view: ImageView, drawableObservable: MutableLiveData<Drawable>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        drawableObservable.observe(parentActivity, Observer { result ->
            view.background = result
        })
    }
}

@BindingAdapter("refreshListener")
fun setRefreshListener(view: SwipeRefreshLayout, viewModel: CatalogueViewModel) {

    view.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
        override fun onRefresh() {
            Timber.d("Called on refresh")
            viewModel.onRefresh()
        }
    })}

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

@BindingAdapter("detailRecyclerAdapter")
fun setDetailSpecsAdapter(view: RecyclerView, adapter: DetailSpecsRecyclerAdapter) {
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

//@BindingAdapter("genericRecyclerAdapter")
//fun setDetailSpecsAdapter(view: RecyclerView, adapter: DetailSpecsRecyclerAdapter) {
//
//    val parentActivity: AppCompatActivity? = view.getParentActivity()
//
//    if (parentActivity != null) {
//        view.setHasFixedSize(true)
//        view.adapter = adapter
//    }
//}


@BindingAdapter("recyclerGridLayoutManager")
fun setGridLayoutManager(view: RecyclerView, gridLayoutManager: GridLayoutManager) {
    view.layoutManager = gridLayoutManager
}

@BindingAdapter("scrollableListener")
fun setScrollableListener(view: RecyclerView, scrollListener: RecyclerView.OnScrollListener) {
        view.addOnScrollListener(scrollListener)
}

@BindingAdapter("languageMutable")
fun setLanguageSelected(view: ConstraintLayout, selectedMutable: MutableLiveData<Boolean>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        selectedMutable.observe(
            parentActivity,
            Observer {
                result ->
                if (result) {
                    val backgroundColor = ContextCompat.getColor(
                        view.context, com.buscatumoto.R.color.colorPrimary_transparent)
                    view.setBackgroundColor(backgroundColor)
                }
            })
    }
}

@BindingAdapter("languageMutable")
fun setLanguageSelected(view: AppCompatCheckBox, selectedMutable: MutableLiveData<Boolean>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        selectedMutable.observe(
            parentActivity,
            Observer {
                    result ->
                view.isChecked = result
            })
    }
}

@BindingAdapter("mutableitemSelected")
fun setMutableItemSelected(view: View, visibility: MutableLiveData<Boolean>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
                if (it) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
        })
    }
}