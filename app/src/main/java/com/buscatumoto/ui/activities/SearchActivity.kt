package com.buscatumoto.ui.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.buscatumoto.R
import com.buscatumoto.databinding.ActivitySearchBinding
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.SearchViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.BasicNavigator
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : LocalizationActivity(),
    HasAndroidInjector, ScreenNavigator {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var basicNavigator: BasicNavigator

    lateinit var searchViewModel: SearchViewModel

    private var errorSnackbar: Snackbar? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var navController: NavController
    private lateinit var mainToolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchViewModel = ViewModelProviders.of(this,
            viewModelFactory).get(SearchViewModel::class.java)
        searchViewModel.lifeCycleOwner = this
        searchViewModel.screenNavigator = this
        binding.viewModel = searchViewModel
        binding.lifecycleOwner = this

        binding.searchIconImgView.setOnClickListener {
            searchViewModel.onSearchRequested(binding.searchEditText?.text.toString())
            hideKeyboardFrom(this@SearchActivity, binding.root)
            searchEditText.setText("")
            sendSearchBtnAnalytics()
        }

        navController = findNavController(R.id.nav_host_fragment_search)
        drawerLayout = findViewById<DrawerLayout>(R.id.searchDrawerLayout)
        mainToolbar = findViewById<Toolbar>(R.id.mainToolbar)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.splashFragment, R.id.containerMainFragment, R.id.catalogueFragment,
                R.id.motoDetailHostFragment), drawerLayout)
        navigationView.setupWithNavController(navController)
        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.searchEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                return when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        if (event?.getAction() == KeyEvent.ACTION_DOWN) {
                            val query = binding.searchEditText?.text.toString()
                            sendSearchTextAnalytics(query)
                            searchViewModel.onSearchRequested(query)
                            hideKeyboardFrom(this@SearchActivity, binding.root)
                            searchEditText.setText("")
                            true
                        } else {
                            false
                        }
                    }
                    else -> {
                        false
                    }
                }
            }
        })

        binding.linearlayouts.languageLinearLayout.setOnClickListener {
            sendDrawerLanguageOptionAnalytics()
            binding.searchDrawerLayout.closeDrawers()
            navController.navigate(R.id.languagePickerFragment)
        }

        searchViewModel.navigateMutable.observe(this, Observer {
            if (it) {
                navController.navigate(R.id.catalogueFragment)
            }
        })

        /**
         * This breaks VMMV rules because view should not command what view model should do.
         * To review and find if there's a better way to update a mutable that depends
         * on the initialisation of a viewmodel. Because the way it was, it could not be changed after.
         * Because it depends of changes made and managed by other view and viewmodel.
         */
        binding.searchDrawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                sendDrawerCloseAnalytics()
            }

            override fun onDrawerOpened(drawerView: View) {
                sendDrawerOpenAnalytics()
                searchViewModel.loadVersionOnDrawerLayout()
            }

        })

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: false
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        navController.navigate(R.id.catalogueFragment)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, searchViewModel.getErrorClickListener())
        errorSnackbar?.show()
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    /**
     * Back navigation is managed through container's view.
     * Basically gets previous Id destination from navController and navigates to it.
     */
    override fun onBackPressed() {
        when (navController.currentBackStackEntry?.destination?.id) {
            R.id.catalogueFragment -> {
                navController.navigate(R.id.containerMainFragment)
            }
            R.id.motoDetailHostFragment -> {
                navController.navigate(R.id.catalogueFragment)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    /**
     * Google Analytics
     */

    private fun sendSearchBtnAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ACTIVITY_SEARCH_BTN_SEARCH_ID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ACTIVITY_SEARCH_SEARCH_BTN_NAME)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, SEARCH_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendDrawerOpenAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ACTIVITY_SEARCH_DRAWER_OPEN_ID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ACTIVITY_SEARCH_DRAWER_OPEN_NAME)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, DRAWER_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendDrawerCloseAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ACTIVITY_SEARCH_DRAWER_CLOSE_ID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ACTIVITY_SEARCH_DRAWER_CLOSE_NAME)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, DRAWER_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendSearchTextAnalytics(query: String) = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ACTIVITY_SEARCH_EDIT_TEXT_SEARCH_ID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ACTIVITY_SEARCH_EDIT_TEXT_SEARCH_NAME)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, SEARCH_CONTENT_TYPE)
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        this.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    private fun sendDrawerLanguageOptionAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DRAWER_OPTION_LANGUAGE_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, DRAWER_CONTENT_TYPE)
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    /**
     * Google Analytics
     */


}