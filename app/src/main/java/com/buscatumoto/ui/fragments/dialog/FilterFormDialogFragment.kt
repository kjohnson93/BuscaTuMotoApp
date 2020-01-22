package com.buscatumoto.ui.fragments.dialog

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.databinding.FragmentFiltroFormBinding
import com.buscatumoto.injection.component.DaggerViewComponent
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.viewmodels.SearchFormViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.FilterFormImpl
import com.buscatumoto.utils.ui.FilterFormMediator
import javax.inject.Inject


class FilterFormDialogFragment: DialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    var closeIButton: ImageButton? = null
    var refreshIButton: ImageButton? = null
    var acceptIButton: ImageButton? = null

    var filterFormMediator: FilterFormMediator? = null

    var filterFormPgBar: ProgressBar? = null

    private var errorSnackbar: Snackbar? = null

    private val injector: ViewModelComponent = DaggerViewModelComponent.builder().networkModule(
        NetworkModule
    ).build()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var searchFormViewModel: SearchFormViewModel
    private lateinit var binding: FragmentFiltroFormBinding

    //Fragment is not destroyed. Only View inside Fragment does. fragment.view will be a different object after fragment goes to background etc.
    //Also, every single View that is implemented a View State Saving/Restoring internally (every view as a default)
    //automatically will be saved and  will restore the state. From the view inflated to every component. Causes it to display just perfectly the same as previous.
    //And also don't forget to assign android:id attribute to every single View placed in the layout that you need to enable View State Saving and Restoring
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.MOTOTAG, "onCreateView called")
        injector.inject(this)
        searchFormViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchFormViewModel::class.java)

        searchFormViewModel.errorMessage.observe(this, Observer { observableValue: Int? ->
            if (observableValue != null) {
                showError(observableValue)
            } else {
                hideError()
            }
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtro_form, container, false)
        binding.viewModel = searchFormViewModel

        return binding.root
    }
//
//    private fun fillSpinnerViews(response: FieldsResponse) {
//
//        //brand
//        var brandArrayList: ArrayList<String> = ArrayList()
//        brandArrayList.addAll(response.brandList)
//        brandArrayList.add(0, "-Marca-")
//        val brandSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
//            brandArrayList)
//        brandSpinner?.adapter = brandSpinnerAdapter
//        brandSpinner?.setSelection(0)
//
//        //modelo placeholder
//        val modelList : ArrayList<String> = ArrayList()
//        modelList.add(0, "Elegir marca")
//        val modelSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
//            modelList)
//        modelSpinner?.adapter = modelSpinnerAdapter
//        modelSpinner?.setSelection(0)
//
//        //bike type
//        val bikeTypeList: ArrayList<String> = ArrayList()
//        bikeTypeList.addAll(response.bikeTypesList)
//        bikeTypeList.removeAt(0)
//        bikeTypeList.add(0, "-Tipo de moto-")
//        val bikeTypeSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bikeTypeList)
//        bikeTypeSpinner?.adapter = bikeTypeSpinnerAdapter
//        bikeTypeSpinner?.setSelection(0)
//
//        //price min
//        val priceMinList: ArrayList<String> = ArrayList()
//        priceMinList.addAll(response.priceMinList as ArrayList<String>)
//        priceMinList.add(0, "-Precio desde-")
//        val priceMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMinList)
//        priceMinSpinner?.adapter = priceMinSpinnerAdapter
//        priceMinSpinner?.setSelection(0)
//
//        //price max
//        val priceMaxList: ArrayList<String> = ArrayList()
//        priceMaxList.addAll(response.priceMaxList as ArrayList<String>)
//        priceMaxList.add(0, "-Precio hasta-")
//        val priceMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMaxList)
//        priceMaxSpinner?.adapter = priceMaxSpinnerAdapter
//        priceMaxSpinner?.setSelection(0)
//
//        //power min
//        val powerMinList: ArrayList<String> = ArrayList()
//        powerMinList.addAll(response.powerMinList as ArrayList<String>)
//        powerMinList.add(0, "-Potencia desde-")
//        val powerMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMinList)
//        powerMinSpinner?.adapter = powerMinSpinnerAdapter
//        powerMinSpinner?.setSelection(0)
//
//        //power max
//        val powerMaxList: ArrayList<String> = ArrayList()
//        powerMaxList.addAll(response.powerMaxList as ArrayList<String>)
//        powerMaxList.add(0, "-Potencia hasta-")
//        val powerMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMaxList)
//        powerMaxSpinner?.adapter = powerMaxSpinnerAdapter
//        powerMaxSpinner?.setSelection(0)
//
//        //cil min
//        val cilMinList: ArrayList<String> = ArrayList()
//        cilMinList.addAll(response.cilMinList as ArrayList<String>)
//        cilMinList.add(0, "-Cilindrada desde-")
//        val cilMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMinList)
//        cilMinSpinner?.adapter = cilMinSpinnerAdapter
//        cilMinSpinner?.setSelection(0)
//
//        //cil max
//        val cilMaxList: ArrayList<String> = ArrayList()
//        cilMaxList.addAll(response.cilMaxList as ArrayList<String>)
//        cilMaxList.add(0, "-Cilindrada hasta-")
//        val cilMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMaxList)
//        cilMaxSpinner?.adapter = cilMaxSpinnerAdapter
//        cilMaxSpinner?.setSelection(0)
//
//        //weight min
//        val weightMinList: ArrayList<String> = ArrayList()
//        weightMinList.addAll(response.weightMinList as ArrayList<String>)
//        weightMinList.add(0, "-Peso desde-")
//        val weightMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMinList)
//        weightMinSpinner?.adapter = weightMinSpinnerAdapter
//        weightMinSpinner?.setSelection(0)
//
//        //weight max
//        val weightMaxList: ArrayList<String> = ArrayList()
//        weightMinList.addAll(response.weightMaxList as ArrayList<String>)
//        weightMaxList.add(0, "-Peso hasta-")
//        val weightMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMaxList)
//        weightMaxSpinner?.adapter = weightMaxSpinnerAdapter
//        weightMaxSpinner?.setSelection(0)
//
//        //year
//        val yearList: ArrayList<String> = ArrayList()
//        yearList.addAll(response.yearList as ArrayList<String>)
//        yearList.add(0, "-Año-")
//        val yearSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, yearList)
//        yearSpinner?.adapter = yearSpinnerAdapter
//        yearSpinner?.setSelection(0)
//
//        //license
//        val licenseTypeList: ArrayList<String> = ArrayList()
//        licenseTypeList.addAll(response.licenses as ArrayList<String>)
//        licenseTypeList.add(0, "-Permiso-")
//        val licenseSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, licenseTypeList)
//        licenseSpinner?.adapter = licenseSpinnerAdapter
//        licenseSpinner?.setSelection(0)
//
//
//    }

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
                filterFormMediator?.notify(refreshIButton?.id, 0)
            }
            R.id.filtrar_accept_ibtn -> {
                //API request and then navigate.
            }
        }
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, searchFormViewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}