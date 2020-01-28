package com.buscatumoto.utils.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import com.buscatumoto.BuscaTuMotoApplication
import javax.inject.Inject


/**
 * Class that manages basic navigation on MyVent App.
 */
class BasicNavigator @Inject constructor() {

    val appContext: Context = BuscaTuMotoApplication.getInstance().applicationContext


    /**
     * Helper method to navigate to any activity.
     * @param classToStartIntent: The class specified to navigate to.
     * @param extras: Any data associated with a intent.
     */
    fun navigateToIntent(activityLauncher: Activity, classToStartIntent: Class<out Any>, extras: Bundle?): Unit{
        val intent : Intent?
        intent = Intent(appContext, classToStartIntent)
        activityLauncher.startActivity(intent)
    }


    /**
     * This method navigates toa fragment in a addtobackstack way. If backstack is popped we return to origin fragment.
     */
    fun navigateAddFragment(fragmentManager: FragmentManager? ,navigateToFragment: Fragment, extras: Bundle? ,tag: String, containerView: Int) {
        val manager = fragmentManager

        Log.d("TAG", "Basic Navigator manager: $manager")

        manager?.let {
            val ft = manager.beginTransaction()

            extras?.let {
                navigateToFragment.arguments = extras
            }

            ft.add(containerView, navigateToFragment)
            ft.addToBackStack(null)
            ft.commit()
        }

    }
}