package com.buscatumoto.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.ActivityMotoDetailBinding
import com.buscatumoto.ui.adapters.DetailViewPagerAdapter
import com.buscatumoto.ui.viewmodels.MotoDetailViewModel
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueUiOp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MotoDetailActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var motoDetailViewModel: MotoDetailViewModel

    private lateinit var binding: ActivityMotoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_moto_detail)

        motoDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MotoDetailViewModel::class.java)
        motoDetailViewModel.lifeCycleOwner = this

        //Assign id from UI -> not good but necessary to avoid creating an additional Dao.
        intent?.extras?.getString(Constants.MOTO_ID_KEY)?.let {
            motoDetailViewModel.id = it
            executeUiOp(CatalogueUiOp.NavigateToDetail(it))
        } ?: run {
            motoDetailViewModel.id = ""
        }

        binding.viewModel = motoDetailViewModel
        binding.lifecycleOwner = this


        //test
        val detailPagerAdapter = DetailViewPagerAdapter(supportFragmentManager)
        binding.detailViewPager.adapter = detailPagerAdapter

        val dotsIndicator = binding.wormDotsIndicator
        dotsIndicator.setViewPager(binding.detailViewPager)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    fun executeUiOp(uiOp: CatalogueUiOp) {
        when (uiOp) {
            is CatalogueUiOp.NavigateToDetail -> {
                motoDetailViewModel.loadMotoDetail(uiOp.id)
            }
        }
    }
}
