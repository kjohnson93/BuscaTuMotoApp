package com.buscatumoto.utils.ui

import android.R
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

//@BindingAdapter("mutableVisibility")
//fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
//    val parentActivity: AppCompatActivity? = view.getParentActivity()
//
//    if (parentActivity != null && visibility != null) {
//        visibility.observe(parentActivity, Observer { value -> view.visibility =  value?: View.VISIBLE})
//    }


@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()


    //Anomymous class method
//    if (parentActivity != null && visibility != null) {
//        visibility.observe(parentActivity, object: Observer<Int> {
//            override fun onChanged(t: Int?) {
//                t?.let {
//                    view.visibility = t
//                }
//            }
//
//        })
//    }

    //Lambda method. Two ways. Auto convert to lambda or convert to lambda manually.
    //Auto
//    if (parentActivity != null && visibility != null) {
//        visibility.observe(parentActivity, Observer<Int> { t ->
//            t?.let {
//                view.visibility = t
//            }
//        })
//    }

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
                spinnerDataObservable.observe(parentActivity, Observer {
                    spinnerDataObservable : List<String>? ->
                    val brandTypeSpinnerAdapter = ArrayAdapter<String>(parentActivity, R.layout.simple_spinner_item, spinnerDataObservable)
                    view?.adapter = brandTypeSpinnerAdapter
                })
        }

        //        val bikeTypeList: ArrayList<String> = ArrayList()
//        bikeTypeList.addAll(response.bikeTypesList)
//        bikeTypeList.removeAt(0)
//        bikeTypeList.add(0, "-Tipo de moto-")
//        val bikeTypeSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bikeTypeList)
//        bikeTypeSpinner?.adapter = bikeTypeSpinnerAdapter
//        bikeTypeSpinner?.setSelection(0)
    }