package com.buscatumoto.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.Constants
import com.buscatumoto.R
import com.buscatumoto.gateway.api.APIConstants
import com.buscatumoto.gateway.api.APIGatewayResponse
import com.buscatumoto.gateway.model.response.FieldsResponse


class FilterFormDialogFragment: DialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    var brandSpinner: Spinner? = null
    var modelSpinner: Spinner? = null
    var bikeTypeSpinner: Spinner? = null
    var priceMinSpinner: Spinner? = null
    var priceMaxSpinner: Spinner? = null
    var powerMinSpinner: Spinner? = null
    var powerMaxSpinner: Spinner? = null
    var cilMinSpinner: Spinner? = null
    var cilMaxSpinner: Spinner? = null
    var weightMinSpinner: Spinner? = null
    var weightMaxSpinner: Spinner? = null
    var yearSpinner: Spinner? = null
    var licenseSpinner: Spinner? = null

    var closeIButton: ImageButton? = null
    var refreshIButton: ImageButton? = null
    var acceptIButton: ImageButton? = null

    var filterFormMediator: FilterFormMediator? = null

    var filterFormPgBar: ProgressBar? = null

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
        var fragmentView = inflater.inflate(R.layout.fragment_filtro_form, container, false)

        bindViews(fragmentView)

        filterFormMediator = FilterFormImpl(activity, brandSpinner, modelSpinner, bikeTypeSpinner,
            priceMinSpinner, priceMaxSpinner, powerMinSpinner, powerMaxSpinner,
            cilMinSpinner, cilMaxSpinner, weightMinSpinner, weightMaxSpinner,
            yearSpinner, licenseSpinner, refreshIButton)

        val buscaTuMotoGateway = BuscaTuMotoApplication.getInstance().buscaTuMotoGateway

        filterFormPgBar?.visibility = View.VISIBLE

        filterFormPgBar?.visibility = View.VISIBLE
        buscaTuMotoGateway?.getFields(object: APIGatewayResponse.SuccessListener<FieldsResponse?> {
            override fun onResponse(response: FieldsResponse?) {
                filterFormPgBar?.visibility = View.GONE
                response?.let {
                    if (response.respuesta == APIConstants.RESPONSE_OK) {
                        fillSpinnerViews(response)
                    }
                }
            }

        }, object : APIGatewayResponse.ErrorListener {
            override fun onError(errorResponse: String?) {
                Log.e(Constants.MOTOTAG, "get fields error: $errorResponse")
                filterFormPgBar?.visibility = View.GONE
            }

        })

        return fragmentView
    }

    private fun fillSpinnerViews(response: FieldsResponse) {
        val brandSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
            response.brandList
        )
        brandSpinner?.adapter = brandSpinnerAdapter
        brandSpinner?.setSelection(0)

        //bike type
        val bikeTypeSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, response.bikeTypesList)
        bikeTypeSpinner?.adapter = bikeTypeSpinnerAdapter
        bikeTypeSpinner?.setSelection(0)

        //price min
            val priceMinSpinnerAdapter = ArrayAdapter<Int>(context, android.R.layout.simple_spinner_item, response.priceMinList)
        priceMinSpinner?.adapter = priceMinSpinnerAdapter
        priceMinSpinner?.setSelection(0)

        //price max
        val priceMaxSpinnerAdapter = ArrayAdapter<Int>(context, android.R.layout.simple_spinner_item, response.priceMaxList)
        priceMaxSpinner?.adapter = priceMaxSpinnerAdapter
        priceMaxSpinner?.setSelection(0)

        //power min
        val powerMinSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.powerMinList)
        powerMinSpinner?.adapter = powerMinSpinnerAdapter
        powerMinSpinner?.setSelection(0)

        //power max
        val powerMaxSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.powerMaxList)
        powerMaxSpinner?.adapter = powerMaxSpinnerAdapter
        powerMaxSpinner?.setSelection(0)

        //cil min
        val cilMinSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.cilMinList)
        cilMinSpinner?.adapter = cilMinSpinnerAdapter
        cilMinSpinner?.setSelection(0)

        //cil max
        val cilMaxSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.cilMaxList)
        cilMaxSpinner?.adapter = cilMaxSpinnerAdapter
        cilMaxSpinner?.setSelection(0)

        //weight min
        val weightMinSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.weightMinList)
        weightMinSpinner?.adapter = weightMinSpinnerAdapter
        weightMinSpinner?.setSelection(0)

        //weight max
        val weightMaxSpinnerAdapter = ArrayAdapter<Float>(context, android.R.layout.simple_spinner_item, response.weightMaxList)
        weightMaxSpinner?.adapter = weightMaxSpinnerAdapter
        weightMaxSpinner?.setSelection(0)

        //year
        val yearSpinnerAdapter = ArrayAdapter<Int>(context, android.R.layout.simple_spinner_item, response.yearList)
        yearSpinner?.adapter = yearSpinnerAdapter
        yearSpinner?.setSelection(0)

        //license
        val licenseSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, response.licenses)
        licenseSpinner?.adapter = licenseSpinnerAdapter
        licenseSpinner?.setSelection(0)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.MOTOTAG, "onActivityCreated called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constants.MOTOTAG, "onStart called")
    }

    private fun bindViews(fragmentView: View) {
        brandSpinner = fragmentView.findViewById(R.id.brandSpinner)
        modelSpinner = fragmentView.findViewById(R.id.modelSpinner)
        bikeTypeSpinner = fragmentView.findViewById(R.id.bikeTypeSpinner)
        priceMinSpinner = fragmentView.findViewById(R.id.priceMinSpinner)
        priceMaxSpinner = fragmentView.findViewById(R.id.priceMaxSpinner)
        powerMinSpinner = fragmentView.findViewById(R.id.powerMinSpinner)
        powerMaxSpinner = fragmentView.findViewById(R.id.powerMaxSpinner)
        cilMinSpinner = fragmentView.findViewById(R.id.cilMinSpinner)
        cilMaxSpinner = fragmentView.findViewById(R.id.cilMaxSpinner)
        weightMinSpinner = fragmentView.findViewById(R.id.weightMinSpinner)
        weightMaxSpinner = fragmentView.findViewById(R.id.weightMaxSpinner)
        yearSpinner = fragmentView.findViewById(R.id.yearSpinner)
        licenseSpinner = fragmentView.findViewById(R.id.licenseSpinner)

        closeIButton = fragmentView.findViewById(R.id.filtrar_close_ibtn)
        refreshIButton = fragmentView.findViewById(R.id.filtrar_refresh_ibtn)
        acceptIButton = fragmentView.findViewById(R.id.filtrar_accept_ibtn)

        filterFormPgBar = fragmentView.findViewById(R.id.filter_dialog_progressBar)

        closeIButton?.setOnClickListener(this)
        refreshIButton?.setOnClickListener(this)
        acceptIButton?.setOnClickListener(this)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //Dialog fragment bug: In order to make setSelection work on a restore instance case, we have to set animation to false.
//        powerMaxSpinner?.setSelection(0, false)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.filtrar_close_ibtn -> {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}