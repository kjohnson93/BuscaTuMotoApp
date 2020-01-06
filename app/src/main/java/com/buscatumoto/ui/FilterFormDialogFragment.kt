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

        //brand
        var brandArrayList: ArrayList<String> = ArrayList()
        brandArrayList.addAll(response.brandList)
        brandArrayList.add(0, "-Marca-")
        val brandSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
            brandArrayList)
        brandSpinner?.adapter = brandSpinnerAdapter
        brandSpinner?.setSelection(0)

        //modelo placeholder
        val modelList : ArrayList<String> = ArrayList()
        modelList.add(0, "Elegir marca")
        val modelSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
            modelList)
        modelSpinner?.adapter = modelSpinnerAdapter
        modelSpinner?.setSelection(0)

        //bike type
        val bikeTypeList: ArrayList<String> = ArrayList()
        bikeTypeList.addAll(response.bikeTypesList)
        bikeTypeList.removeAt(0)
        bikeTypeList.add(0, "-Tipo de moto-")
        val bikeTypeSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bikeTypeList)
        bikeTypeSpinner?.adapter = bikeTypeSpinnerAdapter
        bikeTypeSpinner?.setSelection(0)

        //price min
        val priceMinList: ArrayList<String> = ArrayList()
        priceMinList.addAll(response.priceMinList as ArrayList<String>)
        priceMinList.add(0, "-Precio desde-")
        val priceMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMinList)
        priceMinSpinner?.adapter = priceMinSpinnerAdapter
        priceMinSpinner?.setSelection(0)

        //price max
        val priceMaxList: ArrayList<String> = ArrayList()
        priceMaxList.addAll(response.priceMaxList as ArrayList<String>)
        priceMaxList.add(0, "-Precio hasta-")
        val priceMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMaxList)
        priceMaxSpinner?.adapter = priceMaxSpinnerAdapter
        priceMaxSpinner?.setSelection(0)

        //power min
        val powerMinList: ArrayList<String> = ArrayList()
        powerMinList.addAll(response.powerMinList as ArrayList<String>)
        powerMinList.add(0, "-Potencia desde-")
        val powerMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMinList)
        powerMinSpinner?.adapter = powerMinSpinnerAdapter
        powerMinSpinner?.setSelection(0)

        //power max
        val powerMaxList: ArrayList<String> = ArrayList()
        powerMaxList.addAll(response.powerMaxList as ArrayList<String>)
        powerMaxList.add(0, "-Potencia hasta-")
        val powerMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMaxList)
        powerMaxSpinner?.adapter = powerMaxSpinnerAdapter
        powerMaxSpinner?.setSelection(0)

        //cil min
        val cilMinList: ArrayList<String> = ArrayList()
        cilMinList.addAll(response.cilMinList as ArrayList<String>)
        cilMinList.add(0, "-Cilindrada desde-")
        val cilMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMinList)
        cilMinSpinner?.adapter = cilMinSpinnerAdapter
        cilMinSpinner?.setSelection(0)

        //cil max
        val cilMaxList: ArrayList<String> = ArrayList()
        cilMaxList.addAll(response.cilMaxList as ArrayList<String>)
        cilMaxList.add(0, "-Cilindrada hasta-")
        val cilMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMaxList)
        cilMaxSpinner?.adapter = cilMaxSpinnerAdapter
        cilMaxSpinner?.setSelection(0)

        //weight min
        val weightMinList: ArrayList<String> = ArrayList()
        weightMinList.addAll(response.weightMinList as ArrayList<String>)
        weightMinList.add(0, "-Peso desde-")
        val weightMinSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMinList)
        weightMinSpinner?.adapter = weightMinSpinnerAdapter
        weightMinSpinner?.setSelection(0)

        //weight max
        val weightMaxList: ArrayList<String> = ArrayList()
        weightMinList.addAll(response.weightMaxList as ArrayList<String>)
        weightMaxList.add(0, "-Peso hasta-")
        val weightMaxSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMaxList)
        weightMaxSpinner?.adapter = weightMaxSpinnerAdapter
        weightMaxSpinner?.setSelection(0)

        //year
        val yearList: ArrayList<String> = ArrayList()
        yearList.addAll(response.yearList as ArrayList<String>)
        yearList.add(0, "-Año-")
        val yearSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, yearList)
        yearSpinner?.adapter = yearSpinnerAdapter
        yearSpinner?.setSelection(0)

        //license
        val licenseTypeList: ArrayList<String> = ArrayList()
        licenseTypeList.addAll(response.licenses as ArrayList<String>)
        licenseTypeList.add(0, "-Permiso-")
        val licenseSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, licenseTypeList)
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