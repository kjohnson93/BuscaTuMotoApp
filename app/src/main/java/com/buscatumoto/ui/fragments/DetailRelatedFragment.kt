package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailRelatedFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.DetailRelatedViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject

class DetailRelatedFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: DetailRelatedFragmentBinding

    private lateinit var viewModel: DetailRelatedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_related_fragment, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailRelatedViewModel::class.java)
        viewModel.lifeCycleOwner = this

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }
}