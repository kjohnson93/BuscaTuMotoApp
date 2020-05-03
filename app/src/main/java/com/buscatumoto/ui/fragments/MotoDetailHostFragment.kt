package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentHostMotoDetailBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.DetailViewPagerAdapter
import com.buscatumoto.ui.viewmodels.MotoDetailViewModel
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueUiOp
import javax.inject.Inject

class MotoDetailHostFragment: Fragment(), Injectable {

    companion object {
        fun newInstance(): MotoDetailHostFragment {
            return MotoDetailHostFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var motoDetailViewModel: MotoDetailViewModel
    private lateinit var binding: FragmentHostMotoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host_moto_detail, container, false)

        motoDetailViewModel =  ViewModelProviders.of(this, viewModelFactory).get(MotoDetailViewModel::class.java)
        motoDetailViewModel.lifeCycleOwner = this
        motoDetailViewModel.tempLifeCyclerOwner = this
        binding.viewModel = motoDetailViewModel
        binding.lifecycleOwner = this

        //Assign id from UI -> not good but necessary to avoid creating an additional Dao.
       arguments?.getString(Constants.MOTO_ID_KEY)?.let {
            executeUiOp(CatalogueUiOp.LoadDetailActivity(it))
        }

        return binding.root
    }

    fun bindAdapter(detailViewPagerAdapter: DetailViewPagerAdapter) {
        binding.detailViewPager.adapter = detailViewPagerAdapter
        val dotsIndicator = binding.wormDotsIndicator
        dotsIndicator.setViewPager(binding.detailViewPager)
    }

    fun executeUiOp(uiOp: CatalogueUiOp) {
        when (uiOp) {
            is CatalogueUiOp.LoadDetailActivity -> {
                motoDetailViewModel.loadMotoDetail(uiOp.id, requireActivity().supportFragmentManager)
            }
        }
    }


}