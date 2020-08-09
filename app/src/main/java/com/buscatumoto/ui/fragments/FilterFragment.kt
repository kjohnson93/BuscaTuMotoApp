package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentFilterBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.FilterViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject

class FilterFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: FragmentFilterBinding

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
        val verticalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.fragmentFiltTypeList.layoutManager = verticalLayoutManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        /**
         * Observer section
         */
    }
}