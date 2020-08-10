package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.ui.viewmodels.FilterViewModel
import android.content.Context
import android.view.View
import com.buscatumoto.databinding.RecyclerBiketypeItemBinding
import timber.log.Timber


class FilterRecyclerAdapter(private val filterItemClickListener: FilterItemClickListener): RecyclerView.Adapter<FilterRecyclerAdapter.FilterRecyclerViewHolder>() {

    lateinit var filterItemsList : List<TestRecyclerItemData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRecyclerViewHolder {
        val binding: RecyclerBiketypeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recycler_biketype_item, parent, false)
        return FilterRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterRecyclerViewHolder, position: Int) {
        holder.bind(filterItemsList[position])
    }

    override fun getItemCount(): Int {
        return if (::filterItemsList.isInitialized) filterItemsList.size else 0
    }

    fun updateFilterItemsList(data: List<TestRecyclerItemData>) {
        this.filterItemsList = data
        notifyDataSetChanged()
    }

    //ViewHolder are not meant to having a view model.
    inner class FilterRecyclerViewHolder(
        val binding: RecyclerBiketypeItemBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var filterItem: TestRecyclerItemData

        fun bind(filterItem: TestRecyclerItemData) {
            this.filterItem = filterItem
            binding.itemCircleImg.setImageDrawable(filterItem.drawable)
            binding.itemCircleText.text = filterItem.title
            binding.root.setOnClickListener(this)
            if (filterItem.isSelected) {
                binding.itemCheckImg.visibility = View.VISIBLE
            } else {
                binding.itemCheckImg.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            filterItemClickListener.onClick(filterItem, layoutPosition)
        }
    }

    interface FilterItemClickListener {
        fun onClick(filterItem: TestRecyclerItemData, position: Int)
    }
}