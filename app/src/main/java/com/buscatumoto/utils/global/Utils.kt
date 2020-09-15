package com.buscatumoto.utils.global

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.buscatumoto.R
import com.google.android.gms.ads.AdSize
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun hideKeyboardFrom(
    context: Context,
    view: View?
) {
    val imm =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun chainIsNumeric(chain: String): Boolean {
    var numeric = true

    try {
        val num = Double.parseDouble(chain)
    } catch (e: NumberFormatException) {
        numeric = false
    }

    return numeric
}

fun getDateUtc(now :Boolean): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    when(now){
        true -> return sdf.format(Date())
        false -> {val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, -1)
            return  sdf.format(cal.time)}
    }
}

fun getDateUtcYearsAgo(now :Boolean, years: Int): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    when(now){
        true -> return sdf.format(Date())
        false -> {val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -years)
            return  sdf.format(cal.time)}
    }
}



fun validateNameLengthForApi(nombre: String): Boolean {

    var result = false

    if (nombre.length >= 5) {
        result = true
    }

    return result
}

fun validateNameNumeric(nombre: String): Boolean {
    var result = false

    if (!chainIsNumeric(nombre)) {
        result = true
    }

    return result
}

fun validateNameForApi(nombre: String): Boolean {

    var result = false

    if (nombre.length >= 5 && !chainIsNumeric(nombre)) {
        result = true
    }

    return result
}

fun removeEmptyValues(list: List<String>): ArrayList<String> {
    val mutableList = list as ArrayList

    //Using iterator to avoid ConcurrentModificationException
    with(mutableList.iterator()) {
        forEach {
            if (it  == "") {
                remove()
            }
        }
    }

    return mutableList
}

/**
 * UI UTILS
 */

fun slideUpAnimation (context: Context): Animation {
    return AnimationUtils.loadAnimation(context, R.anim.slide_up)
}

fun slideDownAnimation (context: Context): Animation {
    return AnimationUtils.loadAnimation(context, R.anim.slide_down)
}

fun fadeInAnimation(context: Context): Animation {
    return AnimationUtils.loadAnimation(context, R.anim.fade_in)
}

fun fadeOutAnimation(context: Context): Animation {
    return AnimationUtils.loadAnimation(context, R.anim.fade_out)
}

fun getAdSize(activity: Activity): AdSize? {
    // Step 2 - Determine the screen width (less decorations) to use for the ad width.
    val display: Display = activity.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val widthPixels = outMetrics.widthPixels.toFloat()
    val density = outMetrics.density
    val adWidth = (widthPixels / density).toInt()

    // Step 3 - Get adaptive ad size and return for setting on the ad view.
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
}

/**
 * UI UTILS
 */

