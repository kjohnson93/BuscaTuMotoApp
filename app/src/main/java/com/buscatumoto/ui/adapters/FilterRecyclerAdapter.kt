package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.ui.viewmodels.FilterViewModel
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.buscatumoto.databinding.RecyclerBiketypeItemBinding


class FilterRecyclerAdapter(): RecyclerView.Adapter<FilterRecyclerAdapter.FilterRecyclerViewHolder>() {

    lateinit var filterItemsList : List<TestRecyclerItemData>
    lateinit var viewModel: FilterViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRecyclerViewHolder {

        //binding from xml
        val binding: RecyclerBiketypeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recycler_biketype_item, parent, false)
//        binding.lifecycleOwner = this@FilterRecyclerAdapter.lifecycleOwner

        return FilterRecyclerViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: FilterRecyclerViewHolder, position: Int) {
        viewModel = ViewModelProviders.of((holder.context as FragmentActivity)).get(FilterViewModel::class.java)
        holder.setViewModel(viewModel)
        holder.binding.itemCircleImg.setImageDrawable(filterItemsList[position].drawable)
        holder.binding.itemCircleText.text = filterItemsList[position].title
    }

    override fun getItemCount(): Int {
        return if (::filterItemsList.isInitialized) filterItemsList.size else 0
    }


    fun updateFilterItemsList(data: List<TestRecyclerItemData>) {
        this.filterItemsList = data
        notifyDataSetChanged()
    }

    inner class FilterRecyclerViewHolder(val binding: RecyclerBiketypeItemBinding, val context: Context):
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var filterViewModel: FilterViewModel

        public fun setViewModel(filterViewModel: FilterViewModel) {
            this.filterViewModel = filterViewModel
        }
    }
}