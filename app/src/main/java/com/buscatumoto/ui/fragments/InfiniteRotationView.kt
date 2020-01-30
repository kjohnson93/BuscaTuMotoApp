package com.buscatumoto.ui.fragments


import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.buscatumoto.R
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by tomoaki on 2017/08/13.
 */
class InfiniteRotationView(context: Context, attributeSet: AttributeSet)
    : RelativeLayout(context, attributeSet) {

    internal lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    private val layoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var onScrollListener: OnScrollListener

    private var dispose: Disposable? = null

    init {
        val infiniteRotationView = View.inflate(context, R.layout.view_infinite_rotation, this)
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            HORIZONTAL,
            false
        )
        recyclerView = infiniteRotationView.findViewById(R.id.recyclerView_horizontalList)
    }

    fun setAdapter(adapter: SearchBrandsRecyclerAdapter) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val snapHelper = androidx.recyclerview.widget.PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        adapter.itemCount
            .takeIf { it > 1 }
            ?.apply {
                onScrollListener = OnScrollListener(
                    adapter.itemCount,
                    layoutManager,
                    {
                        // When dragging, we assume user swiped. So we will stop auto rotation
                        if (it == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING) {
                            dispose?.dispose()
                        }
                    }
                )
                recyclerView.addOnScrollListener(onScrollListener)
                recyclerView.scrollToPosition(1)
            }
    }

    fun autoScroll(listSize: Int, intervalInMillis: Long) {
        dispose?.let {
            if(!it.isDisposed) return
        }
        dispose = Flowable.interval(intervalInMillis, TimeUnit.MILLISECONDS)
            .map { it % listSize + 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                recyclerView.smoothScrollToPosition(it.toInt() + 1)
            }
    }

    fun stopAutoScroll() {
        dispose?.let(Disposable::dispose)
    }

    class OnScrollListener(
        val itemCount: Int,
        val layoutManager: androidx.recyclerview.widget.LinearLayoutManager,
        val stateChanged: (Int) -> Unit) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()

            if (firstItemVisible > 0 && firstItemVisible % (itemCount - 1) == 0) {
                // When position reaches end of the list, it should go back to the beginning
                recyclerView?.scrollToPosition(1)
            } else if (firstItemVisible == 0) {
                // When position reaches beginning of the list, it should go back to the end
                recyclerView?.scrollToPosition(itemCount - 1)
            }
        }

        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            stateChanged(newState)
        }
    }
}