package com.buscatumoto.ui.adapters

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.databinding.RecyclerItemTextBinding
import com.buscatumoto.utils.global.FILTER_CONTENT_TYPE
import com.google.firebase.analytics.FirebaseAnalytics

class FilterRecyclerTextAdapter(): RecyclerView.Adapter<FilterRecyclerTextAdapter.FilterRecyclerTextViewHolder>() {

    lateinit var filterItemsList : List<FilterRecyclerItem>
    val itemNameSelectedMutable = MutableLiveData<String> ()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRecyclerTextViewHolder {
        val binding: RecyclerItemTextBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_text, parent, false)
        return FilterRecyclerTextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterRecyclerTextViewHolder, position: Int) {
        val filterItem = filterItemsList[position]
        holder.bind(filterItem)
        holder.binding.root.setOnClickListener {

            val isItemSelected = filterItem.isSelected

            if (isItemSelected) {
                filterItem.isSelected = false
                itemNameSelectedMutable.value = ""
            } else {
                filterItemsList.forEach {
                    it.isSelected = false
                }
                filterItem.isSelected = true
                itemNameSelectedMutable.value = filterItem.title
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

    fun clearSelectedValue() {
        this.filterItemsList.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }

    //ViewHolder are not meant to having a view model.
    inner class FilterRecyclerTextViewHolder(
        val binding: RecyclerItemTextBinding
    ):
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var filterItem: FilterRecyclerItem

        fun bind(filterItem: FilterRecyclerItem) {
            this.filterItem = filterItem
            binding.itemCircleImg.setImageDrawable(filterItem.drawable)
            binding.itemCircleTvw.text = filterItem.title
            if (filterItem.isSelected) {
                val firebaseAnalytics = FirebaseAnalytics.getInstance(BuscaTuMotoApplication.getInstance().applicationContext)
                firebaseAnalytics.run {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ITEM_VALUE_SELECTED")
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FILTER_CONTENT_TYPE)
                    bundle.putString(FirebaseAnalytics.Param.VALUE, filterItem.title)
                    this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                }
                binding.itemCheckImg.visibility = View.VISIBLE
                binding.itemCircleImg.setColorFilter(
                    ContextCompat.getColor(
                    BuscaTuMotoApplication.getInstance().applicationContext, R.color.grey_tint), PorterDuff.Mode.MULTIPLY)
            } else {
                binding.itemCheckImg.visibility = View.GONE
                /**
                 * Somehow this line is necessary. The reason is because other holders were applying
                 * a filter when they are not selected.
                 */
                binding.itemCircleImg.clearColorFilter()
            }
        }
    }

}