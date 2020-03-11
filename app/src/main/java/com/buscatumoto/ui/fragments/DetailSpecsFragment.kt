package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailSpecsFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.DetailSpecsViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject

class DetailSpecsFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: DetailSpecsFragmentBinding

    private lateinit var viewModel: DetailSpecsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.detail_specs_fragment, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailSpecsViewModel::class.java)
        viewModel.lifeCyclerOwner = this

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    fun bind(motoDetailUi: MotoDetailUi) {
        viewModel.bind(motoDetailUi)
    }

}