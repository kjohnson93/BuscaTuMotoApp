package com.buscatumoto.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.buscatumoto.R


class FilterFormDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    var spinner: Spinner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_filtro_form, container, false)

        spinner = fragmentView.findViewById(R.id.brandSpinner)
// Create an ArrayAdapter using the string array and a default spinner layout


//        var spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity,)




        return fragmentView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        ArrayAdapter.createFromResource(
            activity,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner?.adapter = adapter
            spinner?.setSelection(0)
        }

    }
}