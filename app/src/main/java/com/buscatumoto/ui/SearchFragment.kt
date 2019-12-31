package com.buscatumoto.ui

import android.os.Bundle
import android.support.v4.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import com.buscatumoto.R


class SearchFragment: Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    var activityReadyListener: ReadyListener? = null

    var bannerLogo: ImageView? = null
    var arrowDownImgBtn: ImageButton? = null

    var dialogFiltoFragment: FilterFormDialogFragment = FilterFormDialogFragment.newInstance()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView: View = inflater.inflate(R.layout.fragment_search, container, false)

        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        arrowDownImgBtn = fragmentView.findViewById(R.id.arrowDownImgBtn)


        var isShown = false



        arrowDownImgBtn?.setOnClickListener(object: View.OnClickListener {

            override fun onClick(v: View?) {
                if (!isShown) {
                    isShown = true

                    val fragmentTransactionAdd = fragmentManager?.beginTransaction()
                    fragmentTransactionAdd?.addToBackStack(null)
                    val prev = fragmentManager?.findFragmentByTag("dialog")
                    if (prev != null)
                    {
                        fragmentTransactionAdd?.remove(prev)
                    }
                    dialogFiltoFragment.show(fragmentTransactionAdd, "dialog")
                } else {
                    isShown = false

                }
            }

        })

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
}