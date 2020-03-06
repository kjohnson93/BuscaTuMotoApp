package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailContentFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.DetailContentViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject

class DetailContentFragment: Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: DetailContentViewModel

    private lateinit var binding: DetailContentFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_content_fragment, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailContentViewModel::class.java)
        viewModel.lifeCycleOwner = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }


}