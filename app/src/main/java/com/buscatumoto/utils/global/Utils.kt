package com.buscatumoto.utils.global

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*


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

