package com.buscatumoto.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.buscatumoto.R

/**
 * Created by tomoaki on 2017/08/13.
 */
//class InfiniteRotationAdapter(itemList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val list: List<Any> = listOf(itemList.last()) + itemList + listOf(itemList.first())
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
//        (holder as? ItemViewHolder)?.let {
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
//        val view = LayoutInflater
//            .from(parent?.context)
//            .inflate(R.layout.brand_highlight_item_row, parent, false)
//        return ItemViewHolder(view)
//    }
//
//    override fun getItemCount() = list.size
//
//    internal class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        lateinit var pageName: TextView
//        lateinit var pagePosition: TextView
//
//    }
//
//
//}