package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.databinding.FragmentCatalogueBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.CatalogueListAdapter
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.utils.data.TotalElementsObject
import com.buscatumoto.utils.global.PAGE_START
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import com.buscatumoto.utils.ui.PaginationListener
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CatalogueFragment : Fragment(), Injectable, ScreenNavigator,
    CatalogueItemClickListener, SwipeRefreshLayout.OnRefreshListener  {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var catalogueViewModel: CatalogueViewModel
    private lateinit var binding: FragmentCatalogueBinding
    var catalogueListAdapter = CatalogueListAdapter(this)


    private var snackbarError: Snackbar? = null

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        enterTransition = inflater.inflateTransition(R.transition.slide_right_combo)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_catalogue, container, false
        )

        catalogueViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CatalogueViewModel::class.java)
        catalogueViewModel.screenNavigator = this
        binding.viewModel = catalogueViewModel
        binding.lifecycleOwner = this
        catalogueViewModel.lifecycleOwner = this

        catalogueViewModel.getErrorMessage().observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                showErrorMessage(errorMessage)
            } else {
                hideError()
            }
        })

        var layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.catalagueContentRv.adapter = catalogueListAdapter
        binding.catalagueContentRv.layoutManager = layoutManager
        binding.catalagueContentRv.addOnScrollListener(getScrollableListener(layoutManager))
        binding.swipeRefresh.setOnRefreshListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catalogueViewModel.catalogueDataIndex.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.catalogueFragmentPbar.visibility = View.GONE
                    binding.catalogueNoResults.visibility = View.GONE
                    isLoading = false

                    it.data?.let { list ->
                        if (list.isEmpty()) {
                        } else {

                            if (currentPage != PAGE_START) {
                                catalogueListAdapter.removeLoading()
                            }

                            binding.catalogueNoResults.visibility = View.GONE
                            catalogueListAdapter.addItems(list)
                            binding.swipeRefresh.isRefreshing = false
                            //layoutManager = null
                        }
                    }
                }
                Result.Status.LOADING -> {
                    if (currentPage != PAGE_START) {
                        catalogueListAdapter.addLoading()
                    } else {
                        //Show global loading
                        binding.catalogueFragmentPbar.visibility = View.VISIBLE
                    }
                }
                Result.Status.ERROR -> {
                    binding.catalogueFragmentPbar.visibility = View.GONE
                    showErrorMessage(it.message)
                    //layoutManager = null
                }
            }
        })

        catalogueViewModel.isLastPageLiveData.observe(viewLifecycleOwner, Observer {
                result ->
            isLastPage = true
        })

        catalogueViewModel.currentPageLiveData.observe(viewLifecycleOwner, Observer {
            result ->
            this.currentPage = result
        })

        catalogueViewModel.pageLoadingLiveData.observe(viewLifecycleOwner, Observer {
            result ->
            if (result) {
                if (currentPage != PAGE_START) {
                    catalogueListAdapter.addLoading()
                } else {
                    //Show global loading
                    binding.catalogueFragmentPbar.visibility = View.VISIBLE
                }
            }
        })

        TotalElementsObject.mutableTest.observe(viewLifecycleOwner, Observer {
            result ->
            if (result == 0 && !isLastPage) {
                binding.catalogueNoResults.visibility = View.VISIBLE
            } else {
                binding.catalogueNoResults.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(
            R.id.action_catalogueFragment_to_motoDetailHostFragment,
            extras
        )
    }

    private fun hideError() {
        snackbarError?.dismiss()
    }

    private fun showErrorMessage(errorMessage: String?) {
        snackbarError =
            Snackbar.make(binding.root, errorMessage.toString(), Snackbar.LENGTH_INDEFINITE)
        snackbarError?.setAction(R.string.retry, catalogueViewModel.retryClickListener)
        snackbarError?.show()
    }

    private fun getScrollableListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {

        return object: PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                catalogueViewModel.loadMoreItemsEvent()
            }
            override fun isLastPage(): Boolean {
                return isLastPage
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
        }
    }

    override fun onItemClick(id: String) {
        catalogueViewModel.onItemClick(id)
    }

    override fun onRefresh() {
        currentPage = PaginationListener.PAGE_START;
        isLastPage = false
    }


}