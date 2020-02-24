package com.buscatumoto.ui.activities

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.R
import com.buscatumoto.databinding.ActivityCatalogueBinding
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.PaginationListener
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import kotlinx.android.synthetic.main.activity_catalogue.*
import kotlinx.android.synthetic.main.content_catalogue.view.*
import javax.inject.Inject

class CatalogueActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var catalogueViewModel: CatalogueViewModel

    private lateinit var binding: ActivityCatalogueBinding

    private var snackbarError: Snackbar? = null

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var catalogueRV: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_catalogue)

        catalogueViewModel = ViewModelProviders.of(this, viewModelFactory).get(CatalogueViewModel::class.java)
        binding.viewModel = catalogueViewModel
        binding.lifecycleOwner = this
        catalogueViewModel.lifecycleOwner = this

        swipeRefreshLayout = binding.root.swipeRefresh
        catalogueRV = binding.root.catalague_content_rv

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        catalogueRV?.layoutManager = layoutManager

        catalogueRV?.addOnScrollListener(object: PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                catalogueViewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean {
                return catalogueViewModel.isLastPage()
            }

            override fun isLoading(): Boolean {
                return catalogueViewModel.isLoading()
            }

        })



        setSupportActionBar(toolbar)

        //Subscribe for error observable
        catalogueViewModel.getErrorMessage().observe(this, Observer {
            errorMessage ->
            if (errorMessage != null) {
                showErrorMessage(errorMessage)
            } else {
                hideErrorMessage()
            }
        })
    }

    private fun hideErrorMessage() {
        snackbarError?.dismiss()
    }

    private fun showErrorMessage(errorMessage: String?) {
        snackbarError = Snackbar.make(binding.root, errorMessage.toString(), Snackbar.LENGTH_INDEFINITE)
        snackbarError?.setAction(R.string.retry, catalogueViewModel.retryClickListener)
        snackbarError?.show()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}
