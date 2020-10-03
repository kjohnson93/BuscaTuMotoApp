package com.buscatumoto.ui.adapters

import android.graphics.drawable.Drawable

data class FilterRecyclerItem (
    val title: String?,
    val drawable: Drawable?,
    var isSelected: Boolean = false
)