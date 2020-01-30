package com.buscatumoto.ui.fragments.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentFiltroFormBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import com.buscatumoto.utils.injection.ViewModelFactory

import javax.inject.Inject


class FilterFormDialogFragment: androidx.fragment.app.DialogFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener, Injectable {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    private var errorSnackbar: Snackbar? = null

    private val injector: ViewModelComponent = DaggerViewModelComponent.builder().networkModule(
        NetworkModule
    ).build()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var searchFormViewModel: SearchFormViewModel
    private lateinit var binding: FragmentFiltroFormBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.MOTOTAG, "onCreateView called")
        searchFormViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchFormViewModel::class.java)

        searchFormViewModel.getErrorMessage().observe(this, Observer { observableValue: Int? ->
            if (observableValue != null) {
                showError(observableValue)
            } else {
                hideError()
            }
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtro_form, container, false)
        binding.viewModel = searchFormViewModel

        binding.filtrarCloseIbtn.setOnClickListener(this)
        binding.filtrarRefreshIbtn.setOnClickListener(this)
        binding.filtrarAcceptIbtn.setOnClickListener(this)
        binding.brandSpinner.onItemSelectedListener = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.MOTOTAG, "onActivityCreated called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constants.MOTOTAG, "onStart called")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.filtrarCloseIbtn.id -> {
                dismiss()
            }
            R.id.filtrar_refresh_ibtn -> {
                //OK: View only can notify view model. View doest no pass any view related classes to view model.
                searchFormViewModel.refreshData()
            }
            R.id.filtrar_accept_ibtn -> {
                //API request and then navigate.
            }
        }
    }

    private fun showError(@StringRes errorMessage: Int) {
//        errorSnackbar = Snackbar.make(binding., errorMessage, Snackbar.LENGTH_INDEFINITE)
//        errorSnackbar?.setAction(R.string.retry, searchFormViewModel.getErrorClickListener())
//        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(Constants.MOTOTAG, "Position clicked: $position")

        when(parent?.id) {
            binding.brandSpinner.id -> {
                val brand = parent?.getItemAtPosition(position).toString()

                if (brand.equals("-Marca-")) {
                    return
                }
                binding.viewModel?.loadModelsByBrand(brand)
            }
        }
    }
}