package com.buscatumoto.ui.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.buscatumoto.R
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.utils.ui.CustomScrollView
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(),
    SearchFragment.ReadyListener, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var navController: NavController
    private lateinit var mainToolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        navController = findNavController(R.id.nav_host_fragment_search)
        drawerLayout = findViewById<DrawerLayout>(R.id.searchDrawerLayout)
        mainToolbar = findViewById<Toolbar>(R.id.mainToolbar)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.splashFragment, R.id.containerMainFragment, R.id.catalogueFragment,
                R.id.motoDetailHostFragment), drawerLayout)
        navigationView.setupWithNavController(navController)
        mainToolbar.setupWithNavController(navController, appBarConfiguration)

//        configureNavigationDrawer()
//        configureToolbar()
    }

    private fun configureToolbar() {
        setSupportActionBar(mainToolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_128)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureNavigationDrawer() {
//        navigationView.setNavigationItemSelectedListener(
//            object: NavigationView.OnNavigationItemSelectedListener {
//                override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//                }
//
//            })
    }

    override fun onReady() {
//        val autoScrollDelay = 2
//        Handler().postDelayed(Runnable {
//            searchBarLayout!!.setExpanded(false, true)
//
//            val hideHeaderDelay = 0.2
//
//            Handler().postDelayed({
////                searchBarLayout!!.visibility = View.GONE
//            }, (hideHeaderDelay * 1000).toLong())
//        }, (autoScrollDelay * 1000).toLong())
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