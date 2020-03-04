package com.buscatumoto.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.ActivityMotoDetailBinding
import com.buscatumoto.ui.viewmodels.MotoDetailViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
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
        binding.viewModel = motoDetailViewModel
        binding.lifecycleOwner = this
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
