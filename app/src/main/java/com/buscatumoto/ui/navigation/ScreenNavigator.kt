package com.buscatumoto.ui.navigation

import android.os.Bundle


interface ScreenNavigator {

    fun navigateToNext(event: Int, extras: Bundle?)
}