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
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailContentFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.activities.SearchActivity
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.DetailContentViewModel
import com.buscatumoto.utils.global.*
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

        viewModel.powerMutable.observe(viewLifecycleOwner, Observer {
            when (it) {
                POWER_UNKNOWN -> {
                    binding.highlightPowerValue.text = NOT_DETERMINED_VALUE
                }
                else -> {
                    binding.highlightPowerValue.text = addMagnitudePower(it.toString())
                }
            }
        })

        viewModel.priceMutable.observe(viewLifecycleOwner, Observer {
            when (it) {
                PRICE_UNKNOWN -> {
                    binding.highlightPriceValue.text = NOT_DETERMINED_VALUE
                }
                else -> {
                    binding.highlightPriceValue.text = addMagnitudePrice(it.toString())
                }
            }
        })

        /**
         * Observer section
         */
        binding.detailPriceDescTvw.movementMethod = ScrollingMovementMethod()
    }

    private fun addMagnitudePrice(sequence: String): String {
        var result = sequence
        var lang = BuscaTuMotoApplication.getInstance().getDefaultLanguage()

        when (lang.language) {
            LANGUAGE_ENG -> {
                result = sequence.plus(" $PRICE_ENGLISH")
            }
            LANGUAGE_ES -> {
                result = sequence.plus(" $PRICE_ES_CA")
            }
            LANGUAGE_CA -> {
                result = sequence.plus(" $PRICE_ES_CA")
            }
        }

        return result
    }

    private fun addMagnitudePower(sequence: String): String {
        var result = sequence
        var lang = BuscaTuMotoApplication.getInstance().getDefaultLanguage()

        when (lang.language) {
            LANGUAGE_ENG -> {
                result = sequence.plus(" $POWER_ENGLISH")
            }
            LANGUAGE_ES -> {
                result = sequence.plus(" $POWER_ES_CA")
            }
            LANGUAGE_CA -> {
                result = sequence.plus(" $POWER_ES_CA")
            }
        }

        return result
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