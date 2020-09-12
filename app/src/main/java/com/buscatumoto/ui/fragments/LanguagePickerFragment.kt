package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentLanguagePickerBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.activities.SearchActivity
import com.buscatumoto.ui.viewmodels.LanguagePickerViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*
import javax.inject.Inject

class LanguagePickerFragment: BaseFragment(), Injectable {

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
        binding.ca = LANGUAGE_CA
        binding.es = LANGUAGE_ES
        binding.en = LANGUAGE_ENG

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LanguagePickerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.languageSelectedMutable.observe(viewLifecycleOwner, Observer {
            sendLanguageSelectedAnalytics(it)
            val activity = requireActivity() as SearchActivity
            activity.setLanguage(it)
        })

        return binding.root
    }

    private fun sendLanguageSelectedAnalytics(lang: String) {
        when (lang) {
            LANGUAGE_ES -> {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LANGUAGE_ES_ITEM_ID)
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, LANG_CONTENT_TYPE)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            }
            LANGUAGE_ENG -> {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LANGUAGE_EN_ITEM_ID)
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, LANG_CONTENT_TYPE)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            }
            LANGUAGE_CA -> {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LANGUAGE_CA_ITEM_ID)
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, LANG_CONTENT_TYPE)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }
}