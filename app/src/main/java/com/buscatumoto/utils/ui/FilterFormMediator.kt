package com.buscatumoto.utils.ui

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.*
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.utils.global.Constants

interface FilterFormMediator {
    fun notify(senderId: Int?, event: Int)
}


class FilterFormImpl(val activity: Activity?, val progressBar: ProgressBar? = null,  var brandSpinner: Spinner? = null,
                         var modelSpinner: Spinner? = null,
                         var bikeTypeSpinner: Spinner? = null,
                         var priceMinSpinner: Spinner? = null,
                         var priceMaxSpinner: Spinner? = null,
                         var powerMinSpinner: Spinner? = null,
                         var powerMaxSpinner: Spinner? = null,
                         var cilMinSpinner: Spinner? = null,
                         var cilMaxSpinner: Spinner? = null,
                         var weightMinSpinner: Spinner? = null,
                         var weightMaxSpinner: Spinner? = null,
                         var yearSpinner: Spinner? = null,
                         var licenseSpinner: Spinner? = null,
                         var refreshBtn: ImageButton? = null): FilterFormMediator, AdapterView.OnItemSelectedListener {

    override fun notify(senderId: Int?, event: Int) {

        //if sender is brand and event is show:

        //if sender is reset, tell all views to reset

        //if sender is
        if (senderId == refreshBtn?.id) {
            refreshAllViews()
        }
    }



    private fun refreshAllViews() {

        brandSpinner?.setSelection(0)
        modelSpinner?.setSelection(0)
        bikeTypeSpinner?.setSelection(0)
        priceMinSpinner?.setSelection(0)
        priceMaxSpinner?.setSelection(0)
        powerMinSpinner?.setSelection(0)
        powerMaxSpinner?.setSelection(0)
        cilMinSpinner?.setSelection(0)
        cilMaxSpinner?.setSelection(0)
        weightMinSpinner?.setSelection(0)
        weightMaxSpinner?.setSelection(0)
        yearSpinner?.setSelection(0)
        licenseSpinner?.setSelection(0)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(Constants.MOTOTAG, "Position clicked: $position")

        when (parent?.id) {
            brandSpinner?.id -> {
                //API Request
                progressBar?.visibility = View.VISIBLE

                val buscaTuMotoGateway = BuscaTuMotoApplication.getInstance().buscaTuMotoGateway
                val brand = parent?.getItemAtPosition(position).toString()

                buscaTuMotoGateway?.getBikesByBrand(
                    brand,
                    object : APIGatewayResponse.SuccessListener<ArrayList<String>?> {
                        override fun onResponse(response: ArrayList<String>??) {
                            progressBar?.visibility = View.GONE
                            response?.let {
                                Log.d(Constants.MOTOTAG, "get bikes by brand response: $response")
                                fillModelSpiner(response)
                            }
                        }

                    },
                    object : APIGatewayResponse.ErrorListener {
                        override fun onError(errorResponse: String?) {
                            Log.e(Constants.MOTOTAG, "get bikes by brand error: $errorResponse")
                            progressBar?.visibility = View.GONE
                        }

                    })
            }
        }

    }

    private fun fillModelSpiner(response: ArrayList<String>) {
        val modelTypeList: ArrayList<String> = ArrayList()
        modelTypeList.addAll(response)
        modelTypeList.add(0, "-Elegir Marca-")
        val modelTypeSpinnerAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, modelTypeList)
        modelSpinner?.adapter = modelTypeSpinnerAdapter
        modelSpinner?.setSelection(0)
    }


}

