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
import com.buscatumoto.utils.global.EMPTY_SIZE
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: FragmentFilterBinding
    private var errorSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_filter, container, false)
        viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(FilterViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupGridLayoutManagers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationResultBtn.setOnClickListener {
            findNavController().navigate(R.id.catalogueFragment)
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



        viewModel.navigationButtonTextMutable.observe(viewLifecycleOwner, Observer {
            result ->
            if (result.toInt() == EMPTY_SIZE) {
                 binding.navigationResultBtn.text =
                     requireContext().resources.getString(R.string.empty_result)
            } else {
                val showListText = requireContext().resources.
                getString(R.string.filter_btn_navigation).format(result)
                binding.navigationResultBtn.text = showListText
            }
        })

        viewModel.mutableNavigate.observe(viewLifecycleOwner, Observer {
            result ->
            if (result) {
                findNavController().navigate(R.id.catalogueFragment)
            }
        })

        /**
         * Observer section
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
        val brandGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val bikeTypeGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minPriceGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxPriceGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minPowerGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxPowerGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minDisplacementGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxDisplacementLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val minWeightGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val maxWeightLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val yearGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val licenseGridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)


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
}