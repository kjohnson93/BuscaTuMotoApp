package com.buscatumoto.ui.fragments.dialog

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentFiltroFormBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.BasicNavigator

import javax.inject.Inject


class FilterFormDialogFragment: androidx.fragment.app.DialogFragment(), View.OnClickListener, Injectable, ScreenNavigator {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var searchFormViewModel: SearchFormViewModel
    private lateinit var binding: FragmentFiltroFormBinding

    @Inject
    lateinit var basicNavigator: BasicNavigator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchFormViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchFormViewModel::class.java)

        searchFormViewModel.getError().observe(this, Observer {
            result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })

        searchFormViewModel.lifecycleOwner = this

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtro_form, container, false)
        binding.viewModel = searchFormViewModel

        //If a LiveData is in one of the binding expressions and no LifecycleOwner is set,
        // the LiveData will not be observed and updates to it will not be propagated to the UI.
        binding.lifecycleOwner = this

        binding.filtrarCloseIbtn.setOnClickListener(this)
        binding.filtrarRefreshIbtn.setOnClickListener(this)
        binding.filtrarAcceptIbtn.setOnClickListener(this)

        return binding.root
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
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, searchFormViewModel.getErrorClickListener())
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    override fun navigateToNext(event: Int) {
        when (event) {
            SearchFragment.NAVIGATE_TO_CATALOGUE -> {
                basicNavigator.navigateToIntent(
                    requireActivity(),
                    CatalogueActivity::class.java,
                    null
                )
            }
        }
    }
}