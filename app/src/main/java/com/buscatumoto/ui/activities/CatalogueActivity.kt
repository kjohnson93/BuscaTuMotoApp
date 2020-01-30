package com.buscatumoto.ui.activities

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buscatumoto.R
import com.buscatumoto.databinding.ActivityCatalogueBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.viewmodels.CatalogueViewModel
import com.buscatumoto.utils.injection.ViewModelFactory

import kotlinx.android.synthetic.main.activity_catalogue.*
import javax.inject.Inject

class CatalogueActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var catalogueViewModel: CatalogueViewModel

    private lateinit var binding: ActivityCatalogueBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_catalogue)

        catalogueViewModel = ViewModelProviders.of(this, viewModelFactory).get(CatalogueViewModel::class.java)
        binding.viewModel = catalogueViewModel

        setSupportActionBar(toolbar)
    }

}
