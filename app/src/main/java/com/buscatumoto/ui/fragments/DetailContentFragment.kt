package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailContentFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.DetailContentViewModel
import com.buscatumoto.utils.global.Constants
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
        viewModel.lifeCycleOwner = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //Assign id from UI -> not good but necessary to avoid creating an additional Dao.
        arguments?.getString(Constants.MOTO_ID_KEY)?.let { id ->
            //get parcelable motodetail and pass it to viewModel
            arguments?.getParcelable<MotoDetailUi>(Constants.MOTO_DETAIL_UI_KEY)?.let {motoDetailUid ->
                executeUiOp(CatalogueUiOp.LoadFragmentPageContent(id, motoDetailUid))
            }
        }

        binding.detailPriceDescTvw.movementMethod = ScrollingMovementMethod()
        binding.detailMainDesc.movementMethod = ScrollingMovementMethod()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    fun executeUiOp(uiOp: CatalogueUiOp) {
        when (uiOp) {
            is CatalogueUiOp.LoadFragmentPageContent -> {
                viewModel.bind(uiOp.motoDetailUi)
            }
        }
    }


}