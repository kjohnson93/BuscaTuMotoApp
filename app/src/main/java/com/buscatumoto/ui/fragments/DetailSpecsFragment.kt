package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailSpecsFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.DetailSpecsRecyclerAdapter
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.DetailSpecsViewModel
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueUiOp
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

        arguments?.getString(Constants.MOTO_ID_KEY)?.let { id ->
            //get parcelable motodetail and pass it to viewModel
            arguments?.getParcelable<MotoDetailUi>(Constants.MOTO_DETAIL_UI_KEY)?.let { motoDetailUid ->
                executeUiOp(CatalogueUiOp.LoadFragmentPageContent(id, motoDetailUid))
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //gridlayout manager
        val gridLayoutManager = GridLayoutManager(context, 2)
        //TODO notifyDatasetchanged not working on gridlayout.
        binding.detailSpecsRV.layoutManager = gridLayoutManager
        binding.detailSpecsRV.itemAnimator = DefaultItemAnimator()
        binding.detailSpecsRV.adapter = DetailSpecsRecyclerAdapter()


        return binding.root
    }

    fun executeUiOp(uiOp: CatalogueUiOp) {
        when (uiOp) {
            is CatalogueUiOp.LoadFragmentPageContent -> {
                viewModel.bind(uiOp.motoDetailUi)
            }
        }
    }

    fun bind(motoDetailUi: MotoDetailUi) {
        viewModel.bind(motoDetailUi)
    }

}