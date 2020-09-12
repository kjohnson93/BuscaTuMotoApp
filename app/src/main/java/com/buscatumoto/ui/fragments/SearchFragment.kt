package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.view.*
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.RetryErrorModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import javax.inject.Inject


class SearchFragment : BaseFragment(), Injectable,
    ScreenNavigator, SearchBrandsRecyclerAdapter.BrandItemClickListener {

    //Adapters
    private val searchBrandsAdapter = SearchBrandsRecyclerAdapter(this)

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: FrontPageViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
        enterTransition = inflater.inflateTransition(R.transition.slide_right_long)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        loadBrands()

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.brandsRecyclerView.setAdapter(searchBrandsAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val brandListSize = searchBrandsAdapter.itemCount
        binding.brandsRecyclerView.autoScroll(
            brandListSize,
            AUTO_SCROLL_TIME_ELLAPSE_MILLIS
        )

        /**
         * Observer section
         */
        viewModel.navigateMutable.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.catalogueFragment)
            }
        })

        viewModel.errorModelMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })

        val slideUp = slideUpAnimation(requireContext())
        Handler().postDelayed( {
            binding.brandsRecyclerView.visibility = View.VISIBLE
            binding.brandsRecyclerView.startAnimation(slideUp)
        }, 1500)

        viewModel.searchTextMutable.observe(viewLifecycleOwner, Observer {
            hideKeyboardFrom(requireContext(), binding.root)
        })

        /**
         * Observer section
         */
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.brandsRecyclerView.stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(R.id.action_containerMainFragment_to_catalogueFragment, extras)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.getErrorClickListener())
        errorSnackbar?.show()
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val brandNamesTypedArray = context.resources.obtainTypedArray(R.array.filter_brand_names_array)
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.filter_brand_logos_array)

        val  brandRecyclerUiModelList = ArrayList<BrandRecyclerUiModel?>()

        var index = 0

        while (index < drawabletypedArray.length()) {
            val brandRecyclerUiModel = BrandRecyclerUiModel(brandNamesTypedArray.getString(index), drawabletypedArray.getDrawable(index))
            brandRecyclerUiModelList.add(brandRecyclerUiModel)
            index ++
        }

        val modifiedList: List<BrandRecyclerUiModel?> = listOf(brandRecyclerUiModelList.last()) + brandRecyclerUiModelList + listOf(brandRecyclerUiModelList.first())

        searchBrandsAdapter.updateBrandHighLights(modifiedList as List<BrandRecyclerUiModel>)
    }

    override fun onBrandItemClick(brand: String) {
        sendBrandItemSelectedAnalytics()
        viewModel.onBrandItemClick(brand)
    }

    private fun sendBrandItemSelectedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_BRAND_ITEM_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, SEARCH_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}