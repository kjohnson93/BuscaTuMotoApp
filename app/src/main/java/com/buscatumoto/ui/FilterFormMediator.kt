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

}

