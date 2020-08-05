package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentLanguagePickerBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.LanguagePickerViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject

class LanguagePickerFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: LanguagePickerViewModel
    private lateinit var binding: FragmentLanguagePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_language_picker,
        container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LanguagePickerViewModel::class.java)
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