package com.buscatumoto.ui.activities

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.injection.module.ViewModelModule
import com.buscatumoto.utils.ui.CustomScrollView
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.viewmodels.SearchBikeActivityViewModel
import javax.inject.Inject

class SearchActivity: AppCompatActivity(),
    SearchFragment.ReadyListener {

    /**
     * Main view
     */
    private var coordLayout: CoordinatorLayout? = null

    /**
     * App barlayout, purpose:  add  an scrollable header.
     */
    private var searchBarLayout: AppBarLayout? = null
    /**
     * Scrollable section of app bar layout.
     */
    private var collapsingToolbar: CollapsingToolbarLayout? = null
    /**
     * Main content view. Scrollable to be able to collapse/expand header.
     */
    private var nestedScrollView: CustomScrollView? = null

//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//
//        setContentView(R.layout.activity_test)
//
//        searchBarLayout = findViewById(R.id.searchAppBar)
//        collapsingToolbar = findViewById(R.id.collapsingToolbar)
//        coordLayout = findViewById(R.id.searchCoordLayout)
//        nestedScrollView = findViewById(R.id.nestedscrollview)
//
//        nestedScrollView!!.isEnableScrolling = false
//        this.openFragment()
//
//        disableHeaderScroll()
//    }

        private val injector: ViewModelComponent = DaggerViewModelComponent
        .builder().networkModule(NetworkModule).build()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var searchBikeActivityViewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setContentView(R.layout.activity_search)
        searchBikeActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchBikeActivityViewModel::class.java)


        searchBarLayout = findViewById(R.id.searchAppBar)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        coordLayout = findViewById(R.id.searchCoordLayout)
        nestedScrollView = findViewById(R.id.nestedscrollview)

        nestedScrollView!!.isEnableScrolling = false
        this.openFragment()

//        (application as BuscaTuMotoApplication).component.inject(this)


        disableHeaderScroll()
    }

    /**
     * Disables user scroll movements on app bar layout header.
     */
    fun disableHeaderScroll() {
        val params = searchBarLayout!!.layoutParams as CoordinatorLayout.LayoutParams
        if (params.behavior == null)
            params.behavior = AppBarLayout.Behavior()
        val behaviour = params.behavior as AppBarLayout.Behavior
        behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }

    /**
     * Loads fragment using animation on custom scroll view. Bottom of view
     */
    private fun openFragment() {

        var fragment = SearchFragment.newInstance()

        fragment.setActivityDelegate(this)

        var fragmentManager: FragmentManager = this.supportFragmentManager
        var transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_bottom,
            R.anim.slide_out_bottom,
            R.anim.slide_in_bottom,
            R.anim.slide_out_bottom
        )
        transaction.addToBackStack(null)
        transaction.add(R.id.fragmentLL, fragment, "LOGIN FRAGMENT").commit()
    }

    /**
     * Overriding backpressed to finish instead of navigating to Splashscreen
     */
    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }



    override fun onReady() {
        val secondsDelayed = 6
        Handler().postDelayed(Runnable {
            searchBarLayout!!.setExpanded(false, true)
        }, (secondsDelayed * 1000).toLong())
    }

    /**
     * To be reviewed: Method to show error response from server. Handled by container. Could be handled by subview as well.
     */
    override fun showError(error: String?) {
        var toast: Toast = Toast.makeText(applicationContext, error, Toast.LENGTH_LONG)
        toast.show()
    }



}