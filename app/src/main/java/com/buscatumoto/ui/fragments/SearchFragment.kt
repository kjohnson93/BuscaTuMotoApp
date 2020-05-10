package com.buscatumoto.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.activities.SearchActivity
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.BasicNavigator
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject


class SearchFragment : androidx.fragment.app.Fragment(), View.OnClickListener, Injectable,
    ScreenNavigator {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }

        const val NAVIGATE_TO_CATALOGUE = 1
    }

    var activityReadyListener: ReadyListener? = null

    var arrowDownImgBtn: ImageButton? = null
    var filtrarBtn: Button? = null

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var basicNavigator: BasicNavigator

    lateinit var frontPageViewModel: FrontPageViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)
        binding.arrowDownImgBtn.setOnClickListener(this)
        binding.filtrarBtn.setOnClickListener(this)

        frontPageViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        frontPageViewModel.lifeCycleOwner = this
        frontPageViewModel.screenNavigator = this
        binding.viewModel = frontPageViewModel
        binding.lifecycleOwner = this


        binding.codPacienteInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                return when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        frontPageViewModel.onSearchRequested(binding.codPacienteInput?.text.toString())
                        hideSoftKeyboard()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

        })

        frontPageViewModel.getError().observe(viewLifecycleOwner, Observer { result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        arrowDownImgBtn?.setOnClickListener(this)
        filtrarBtn?.setOnClickListener(this)

        val brandListSize = frontPageViewModel.searchBrandsAdapter.itemCount
        binding.fragmentSearchBrandsRv.autoScroll(
            brandListSize,
            Constants.AUTO_SCROLL_TIME_ELLAPSE_MILLIS
        )

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.fragmentSearchBrandsRv.stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Casting to SearchActivity because we don't have a delegate as we did with previous navigation.
        (requireActivity() as SearchActivity).onReady()
    }

    interface ReadyListener {
        fun onReady()
        fun showError(errorResponse: String?) {
        }
    }

    fun setActivityDelegate(activity: ReadyListener) {
        this.activityReadyListener = activity
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.filtrarBtn.id -> {
                findNavController().navigate(R.id.action_searchFragment_to_filterFormDialogFragment)
            }
            binding.arrowDownImgBtn.id -> {
                findNavController().navigate(R.id.action_searchFragment_to_filterFormDialogFragment)
            }
        }
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(R.id.action_searchFragment_to_catalogueFragment, extras)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, frontPageViewModel.getErrorClickListener())
        errorSnackbar?.show()
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view?.getWindowToken(), 0)
    }

}