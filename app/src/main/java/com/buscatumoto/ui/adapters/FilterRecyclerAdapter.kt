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
    lateinit var viewModel: FilterViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRecyclerViewHolder {
        val binding: RecyclerBiketypeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recycler_biketype_item, parent, false)

        return FilterRecyclerViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: FilterRecyclerViewHolder, position: Int) {
        val filterItem = filterItemsList[position]
        holder.binding.itemCircleImg.setImageDrawable(filterItem.drawable)
        holder.binding.itemCircleText.text = filterItem.title
        if (filterItem.isSelected) {
            holder.binding.itemCheckImg.visibility = View.VISIBLE
        } else {
            holder.binding.itemCheckImg.visibility = View.GONE
        }
        holder.binding.root.setOnClickListener {
            filterItemClickListener.onClick(filterItem, position)
        }
    }

    override fun getItemCount(): Int {
        return if (::filterItemsList.isInitialized) filterItemsList.size else 0
    }


    fun updateFilterItemsList(data: List<TestRecyclerItemData>) {
        this.filterItemsList = data
        notifyDataSetChanged()
    }

    inner class FilterRecyclerViewHolder(
        val binding: RecyclerBiketypeItemBinding,
        val context: Context):
        RecyclerView.ViewHolder(binding.root) {

    }

    interface FilterItemClickListener {
        fun onClick(filterItem: TestRecyclerItemData, position: Int)
    }
}