package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentCatalogueBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CatalogueFragment: Fragment(), Injectable, ScreenNavigator {

    companion object {
        fun newInstance(): CatalogueFragment {
            return CatalogueFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var catalogueViewModel: CatalogueViewModel
    private lateinit var binding: FragmentCatalogueBinding

    private var snackbarError: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_catalogue, container, false
        )

        catalogueViewModel = ViewModelProviders.of(this, viewModelFactory).get(CatalogueViewModel::class.java)
        catalogueViewModel.screenNavigator = this
        binding.viewModel = catalogueViewModel
        binding.lifecycleOwner = this
        catalogueViewModel.lifecycleOwner = this

        catalogueViewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            errorMessage ->
            if (errorMessage != null) {
                showErrorMessage(errorMessage)
            } else {
                hideError()
            }
        })



        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(R.id.action_catalogueFragment_to_motoDetailHostFragment, extras)
    }

    private fun hideError() {
        snackbarError?.dismiss()
    }

    private fun showErrorMessage(errorMessage: String?) {
        snackbarError =
            Snackbar.make(binding.root, errorMessage.toString(), Snackbar.LENGTH_INDEFINITE)
        snackbarError?.setAction(R.string.retry, catalogueViewModel.retryClickListener)
        snackbarError?.show()
    }


}