package com.buscatumoto.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * Custom scroll view.
 * Overrides method to prevent scrolling on login container (@LoginActivity)
 */
class CustomScrollView : ScrollView {
    var isEnableScrolling = true

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(context: Context?) : super(context) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnableScrolling) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnableScrolling) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }
}