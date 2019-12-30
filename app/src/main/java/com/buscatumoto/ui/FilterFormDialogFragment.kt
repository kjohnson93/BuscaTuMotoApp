package com.buscatumoto.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buscatumoto.R


class FilterFormDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_filtro_form, container, false)



        return fragmentView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
//        val window = dialog.window ?: return
//        val params = window.attributes
//        params.width = 700
//        params.height = 1200
//        window.attributes = params
    }
}