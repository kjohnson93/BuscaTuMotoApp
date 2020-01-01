package com.buscatumoto.ui

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.buscatumoto.Constants
import com.buscatumoto.R


class SearchFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    var activityReadyListener: ReadyListener? = null

    var bannerLogo: ImageView? = null
    var arrowDownImgBtn: ImageButton? = null
    var filtrarBtn: Button? = null

    var dialogFiltoFragment: FilterFormDialogFragment = FilterFormDialogFragment.newInstance()

    var mLastClickTime: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView: View = inflater.inflate(R.layout.fragment_search, container, false)

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        arrowDownImgBtn = fragmentView.findViewById(R.id.arrowDownImgBtn)
        filtrarBtn = fragmentView.findViewById(R.id.filtrarBtn)


        arrowDownImgBtn?.setOnClickListener(this)
        filtrarBtn?.setOnClickListener(this)


        bannerLogo = fragmentView.findViewById(R.id.imageView3)





        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.activityReadyListener?.let {
            this.activityReadyListener!!.onReady()
        }
    }

    interface ReadyListener {
        fun onReady()
        fun showError(errorResponse: String?) {
        }
    }

    fun setActivityDelegate(activity: ReadyListener) {
        this.activityReadyListener = activity
    }

    fun openFilterDialogFragment() {
        // Make sure the current transaction finishes first
        var isFMnull = true
        isFMnull = fragmentManager == null

        Log.d(Constants.MOTOTAG, "is FM Null $isFMnull")
        fragmentManager?.executePendingTransactions()
//        val fragmentTransactionAdd = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        // If there is no fragment yet with this tag then show it. Otherwise donothing
        //This is to prevent crash if user is clicking too fast
        if (prev == null) {
            dialogFiltoFragment.show(fragmentManager, "dialog")
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.filtrarBtn -> {
                openFilterDialogFragment()

            }
            R.id.arrowDownImgBtn -> {
                openFilterDialogFragment()
            }
        }
    }
}