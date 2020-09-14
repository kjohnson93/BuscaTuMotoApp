package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentFilterBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.FilterViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_filter.*
import timber.log.Timber
import javax.inject.Inject

class FilterFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: FragmentFilterBinding
    private var errorSnackbar: Snackbar? = null
    private lateinit var mAdView: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_filter, container, false
        )
        viewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(FilterViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupGridLayoutManagers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationResultBtn.setOnClickListener {
            sendBtnSelectedAnalytics()
            findNavController().navigate(R.id.catalogueFragment)
        }

        binding.brandCsLayout.setOnClickListener {
            viewModel.onBrandLayoutClick()
            viewModel.brandExpanded.value?.let { value -> sendBrandExpandedFilterAnalytics(value) }
        }

        binding.typeCsLayout.setOnClickListener {
            viewModel.onBikeTypeLayoutClick()
            viewModel.bikeTypeExpanded.value?.let { value -> sendTypeExpandedFilterAnalytics(value) }
        }

        binding.minPriceCsLayout.setOnClickListener {
            viewModel.onMinPriceLayoutClick()
            viewModel.minPriceExpanded.value?.let { value -> sendPriceMinExpandedFilterAnalytics(value) }
        }

        binding.maxPriceCsLayout.setOnClickListener {
            viewModel.onMaxPriceLayoutClick()
            viewModel.maxPriceExpanded.value?.let { value -> sendPriceMaxExpandedFilterAnalytics(value) }
        }

        binding.minPowerCsLayout.setOnClickListener {
            viewModel.onMinPowerLayoutClick()
            viewModel.minPowerExpanded.value?.let { value -> sendPowerMinExpandedFilterAnalytics(value) }
        }

        binding.maxPowerCsLayout.setOnClickListener {
            viewModel.onMaxPowerLayoutClick()
            viewModel.maxPowerExpanded.value?.let { value -> sendPowerMaxExpandedFilterAnalytics(value) }
        }

        binding.minDisplacementCsLayout.setOnClickListener {
            viewModel.onMinDisplacementLayoutClick()
            viewModel.minDisplacementExpanded.value?.let { value -> sendDisplacementMinExpandedFilterAnalytics(value) }
        }

        binding.maxDisplacementCsLayout.setOnClickListener {
            viewModel.onMaxDisplacementLayoutClick()
            viewModel.maxDisplacementExpanded.value?.let { value -> sendDisplacementMaxExpandedFilterAnalytics(value) }
        }

        binding.minWeightCsLayout.setOnClickListener {
            viewModel.onMinWeightLayoutClick()
            viewModel.minWeightExpanded.value?.let { value -> sendWeightMinExpandedFilterAnalytics(value) }
        }

        binding.maxWeightCsLayout.setOnClickListener {
            viewModel.onMaxWeightLayoutClick()
            viewModel.maxWeightExpanded.value?.let { value -> sendWeightMaxExpandedFilterAnalytics(value) }
        }

        binding.yearCsLayout.setOnClickListener {
            viewModel.onYearLayoutClick()
            viewModel.yearExpanded.value?.let { value -> sendYearExpandedFilterAnalytics(value) }
        }

        binding.licenseCsLayout.setOnClickListener {
            viewModel.onLicenseLayoutClick()
            viewModel.licenseExpanded.value?.let { value -> sendLicenseExpandedFilterAnalytics(value) }
        }

        /**
         * Observer section
         */
        viewModel.brandExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.brandArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.brandArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.bikeTypeExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.typeArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.typeArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.minPriceExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.minPriceArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.minPriceArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.maxPriceExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.maxPriceArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.maxPriceArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.minPowerExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.minPowerArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.minPowerArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.maxPowerExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.maxPowerArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.maxPowerArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.minDisplacementExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.minDisplacementArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.minDisplacementArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.maxDisplacementExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.maxDisplacementArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.maxDisplacementArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.minWeightExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.minWeightArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.minWeightArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.maxWeightExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.maxWeightArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.maxWeightArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.yearExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.yearArrowImg.setImageDrawable(arrowUpDrawable)
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.yearArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.licenseExpanded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val arrowUpDrawable = requireContext().getDrawable(R.drawable.icon_arrow_up)
                binding.licenseArrowImg.setImageDrawable(arrowUpDrawable)
                nestedScrollView.post {
                    nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }
            } else {
                val arrowDownDrawable = requireContext().getDrawable(R.drawable.icon_arrow_down)
                binding.licenseArrowImg.setImageDrawable(arrowDownDrawable)
            }
        })

        viewModel.errorMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })



        viewModel.navigationButtonTextMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result.toInt() == EMPTY_SIZE) {
                binding.navigationResultBtn.text =
                    requireContext().resources.getString(R.string.empty_result)
            } else {
                val showListText =
                    requireContext().resources.getString(R.string.filter_btn_navigation)
                        .format(result)
                binding.navigationResultBtn.text = showListText
            }
        })

        viewModel.mutableNavigate.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                findNavController().navigate(R.id.catalogueFragment)
            }
        })

        viewModel.deletedMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                sendDeletedFilterAnalytics()
            }
        })

        /**
         * Observer section
         */

        /**
         * Google ads
         */

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        /**
         * Google ads
         */
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

    private fun setupGridLayoutManagers() {
        val brandGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val bikeTypeGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minPriceGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxPriceGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minPowerGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxPowerGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minDisplacementGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxDisplacementLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minWeightGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxWeightLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val yearGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val licenseGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)


        binding.fragmentFiltBrandList.layoutManager = brandGridLayoutManager
        binding.fragmentFiltTypeList.layoutManager = bikeTypeGridLayoutManager
        binding.fragmentMinPriceList.layoutManager = minPriceGridLayoutManager
        binding.fragmentFiltMaxPriceList.layoutManager = maxPriceGridLayoutManager
        binding.fragmentFiltMinPowerList.layoutManager = minPowerGridLayoutManager
        binding.fragmentFiltMaxPowerList.layoutManager = maxPowerGridLayoutManager
        binding.fragmentFiltMinDisplacementList.layoutManager = minDisplacementGridLayoutManager
        binding.fragmentFiltMaxDisplacementList.layoutManager = maxDisplacementLayoutManager
        binding.fragmentFiltMinWeightList.layoutManager = minWeightGridLayoutManager
        binding.fragmentFiltMaxWeightList.layoutManager = maxWeightLayoutManager
        binding.fragmentFiltYearList.layoutManager = yearGridLayoutManager
        binding.fragmentFiltLicenseList.layoutManager = licenseGridLayoutManager
    }

    /**
     * Google Analytics
     */

    private fun sendDeletedFilterAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_DELETE_BTN_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendBrandExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_BRAND_LIST_EXPAND_ID)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_BRAND_LIST_COLLAPSE_ID)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendTypeExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_TYPE_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_TYPE_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendPriceMinExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_PRICE_MIN_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_PRICE_MIN_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendPriceMaxExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_PRICE_MAX_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_PRICE_MAX_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendPowerMinExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_POWER_MIN_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_POWER_MIN_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendPowerMaxExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_POWER_MAX_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_POWER_MAX_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendDisplacementMinExpandedFilterAnalytics(isExpanded: Boolean) =
        firebaseAnalytics.run {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

            if (isExpanded) {
                bundle.putString(
                    FirebaseAnalytics.Param.ITEM_ID,
                    FILTER_DISPLACEMENT_MIN_LIST_EXPAND
                )
            } else {
                bundle.putString(
                    FirebaseAnalytics.Param.ITEM_ID,
                    FILTER_DISPLACEMENT_MIN_LIST_COLLAPSE
                )
            }

            this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

    private fun sendDisplacementMaxExpandedFilterAnalytics(isExpanded: Boolean) =
        firebaseAnalytics.run {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

            if (isExpanded) {
                bundle.putString(
                    FirebaseAnalytics.Param.ITEM_ID,
                    FILTER_DISPLACEMENT_MAX_LIST_EXPAND
                )
            } else {
                bundle.putString(
                    FirebaseAnalytics.Param.ITEM_ID,
                    FILTER_DISPLACEMENT_MAX_LIST_COLLAPSE
                )
            }

            this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

    private fun sendWeightMinExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_WEIGHT_MIN_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_WEIGHT_MIN_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendWeightMaxExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_WEIGHT_MAX_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_WEIGHT_MAX_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendYearExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_YEAR_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_YEAR_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendLicenseExpandedFilterAnalytics(isExpanded: Boolean) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)

        if (isExpanded) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_LICENSE_LIST_EXPAND)
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_LICENSE_LIST_COLLAPSE)
        }

        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendBtnSelectedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_NAVIGATE_BTN_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    /**
     * Google Analytics
     */

}