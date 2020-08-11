package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import android.view.View
import com.buscatumoto.databinding.RecyclerBiketypeItemBinding


class FilterRecyclerAdapter(): RecyclerView.Adapter<FilterRecyclerAdapter.FilterRecyclerViewHolder>() {

    lateinit var filterItemsList : List<FilterRecyclerItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRecyclerViewHolder {
        val binding: RecyclerBiketypeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recycler_biketype_item, parent, false)
        return FilterRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterRecyclerViewHolder, position: Int) {
        val filterItem = filterItemsList[position]
        holder.bind(filterItem)
        holder.binding.root.setOnClickListener {
            val isItemSelected = filterItem.isSelected

            if (isItemSelected) {
                filterItem.isSelected = false
            } else {
                filterItemsList.forEach {
                    it.isSelected = false
                }
                filterItem.isSelected = true
            }

            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return if (::filterItemsList.isInitialized) filterItemsList.size else 0
    }

    fun updateFilterItemsList(data: List<FilterRecyclerItem>) {
        this.filterItemsList = data
        notifyDataSetChanged()
    }

    //ViewHolder are not meant to having a view model.
    inner class FilterRecyclerViewHolder(
        val binding: RecyclerBiketypeItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var filterItem: FilterRecyclerItem

        fun bind(filterItem: FilterRecyclerItem) {
            this.filterItem = filterItem
            binding.itemCircleImg.setImageDrawable(filterItem.drawable)
            binding.itemCircleText.text = filterItem.title
            if (filterItem.isSelected) {
                binding.itemCheckImg.visibility = View.VISIBLE
            } else {
                binding.itemCheckImg.visibility = View.GONE
            }
        }
    }
}