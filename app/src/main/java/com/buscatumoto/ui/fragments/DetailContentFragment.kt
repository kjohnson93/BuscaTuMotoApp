package com.buscatumoto.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailContentFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.DetailContentViewModel
import com.buscatumoto.utils.global.MOTO_ID_KEY
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueUiOp
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Observer section
         */
        viewModel.priceDescLiveData.observe(viewLifecycleOwner, Observer {
            binding.detailPriceDescTvw.text = parseFromHtml(it)
        })
        viewModel.mainDescLiveData.observe(viewLifecycleOwner, Observer {
            binding.detailMainDesc.text = parseFromHtml(it)
        })


        /**
         * Observer section
         */


        binding.detailPriceDescTvw.movementMethod = ScrollingMovementMethod()
        binding.detailMainDesc.movementMethod = ScrollingMovementMethod()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    fun parseFromHtml(text: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()
        } else {
            Html.fromHtml(text).toString()
        }
    }


}