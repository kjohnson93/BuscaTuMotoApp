package com.buscatumoto.ui.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.buscatumoto.R
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.utils.ui.CustomScrollView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(),
    SearchFragment.ReadyListener, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    /**
     * Main view
     */
    private var coordLayout: androidx.coordinatorlayout.widget.CoordinatorLayout? = null

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

    private var searchToolbar: Toolbar? = null

    private var appBarlayout: AppBarLayout? = null

    private lateinit var navController: NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        searchBarLayout = findViewById(R.id.searchAppBar)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        coordLayout = findViewById(R.id.searchCoordLayout)
        nestedScrollView = findViewById(R.id.nestedscrollview)
        searchToolbar = findViewById(R.id.search_toolbar)
        appBarlayout = findViewById(R.id.searchAppBar)

//        collapsingToolbar?.setExpandedTitleColor(R.style.collapsingToolbarLayoutTitleColor)
        collapsingToolbar?.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColorCollapsed)

        nestedScrollView!!.isEnableScrolling = false
        disableHeaderScroll()

        navController = findNavController(R.id.nav_host_fragment_search)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val appBarConfigurationBack = AppBarConfiguration(setOf(R.id.searchFragment))

//        setupActionBarWithNavController(navController, appBarConfiguration)
        this.collapsingToolbar?.setupWithNavController(searchToolbar!!, navController)

        val navIcon = search_toolbar.navigationIcon?.changingConfigurations

    }

    /**
     * Disables user scroll movements on app bar layout header.
     */
    fun disableHeaderScroll() {
        val params = searchBarLayout!!.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
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

        var fragmentManager: androidx.fragment.app.FragmentManager = this.supportFragmentManager
        var transaction: androidx.fragment.app.FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_bottom,
            R.anim.slide_out_bottom,
            R.anim.slide_in_bottom,
            R.anim.slide_out_bottom
        )
        transaction.addToBackStack(null)
//        transaction.add(R.id.fragmentLL, fragment, "LOGIN FRAGMENT").commit()
    }


    override fun onReady() {
        val autoScrollDelay = 2
        Handler().postDelayed(Runnable {
            searchBarLayout!!.setExpanded(false, true)

            val hideHeaderDelay = 0.2

            Handler().postDelayed({
//                searchBarLayout!!.visibility = View.GONE
            }, (hideHeaderDelay * 1000).toLong())
        }, (autoScrollDelay * 1000).toLong())
    }

    /**
     * To be reviewed: Method to show error response from server. Handled by container. Could be handled by subview as well.
     */
    override fun showError(error: String?) {
        var toast: Toast = Toast.makeText(applicationContext, error, Toast.LENGTH_LONG)
        toast.show()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: false
    }


}