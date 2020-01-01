package com.buscatumoto.ui

import android.app.Activity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.buscatumoto.R

interface FilterFormMediator {
    fun notify(senderId: Int?, event: Int)
}


class FilterFormImpl(val activity: Activity?,    var brandSpinner: Spinner? = null,
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
                         var refreshBtn: ImageButton? = null): FilterFormMediator {

    override fun notify(senderId: Int?, event: Int) {

        //if sender is brand and event is show:

        //if sender is reset, tell all views to reset

        //if sender is
        if (senderId == refreshBtn?.id) {
            refreshAllViews()
        }
    }

    private fun refreshAllViews() {
        ArrayAdapter.createFromResource(
            activity,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            brandSpinner?.adapter = adapter
            brandSpinner?.setSelection(0)
            modelSpinner?.adapter = adapter
            modelSpinner?.setSelection(0)
            bikeTypeSpinner?.adapter = adapter
            bikeTypeSpinner?.setSelection(0)
            priceMinSpinner?.adapter = adapter
            priceMinSpinner?.setSelection(0)
            priceMaxSpinner?.adapter = adapter
            priceMaxSpinner?.setSelection(0)
            powerMinSpinner?.adapter = adapter
            powerMinSpinner?.setSelection(0)
            powerMaxSpinner?.adapter = adapter
            powerMaxSpinner?.setSelection(0)
            cilMinSpinner?.adapter = adapter
            cilMinSpinner?.setSelection(0)
            cilMaxSpinner?.adapter = adapter
            cilMaxSpinner?.setSelection(0)
            weightMinSpinner?.adapter = adapter
            weightMinSpinner?.setSelection(0)
            weightMaxSpinner?.adapter = adapter
            weightMaxSpinner?.setSelection(0)
            yearSpinner?.adapter = adapter
            yearSpinner?.setSelection(0)
            licenseSpinner?.adapter = adapter
            licenseSpinner?.setSelection(0)
        }    }

}

