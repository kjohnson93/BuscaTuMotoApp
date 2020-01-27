package com.buscatumoto.ui.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buscatumoto.R
import com.buscatumoto.databinding.BrandHighlightItemRowBinding
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.viewmodels.FrontPageBrandViewModel

class SearchBrandsRecyclerAdapter(val itemClickListener: BrandItemClickListener): RecyclerView.Adapter<SearchBrandsRecyclerAdapter.BrandViewHolder>() {

    private var brandHighLightList: List<BrandRecyclerUiModel> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding: BrandHighlightItemRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.brand_highlight_item_row, parent, false)

        return BrandViewHolder(binding)
    }

    override fun getItemCount(): Int {
//        return if(::brandHighLightList.isInitialized) brandHighLightList.size else 0
        return brandHighLightList.size
    }

    override fun onBindViewHolder(viewHolder: BrandViewHolder, position: Int) {
        viewHolder.bind(brandHighLightList[position])
    }

    fun updateBrandHighLights(brandsList: List<BrandRecyclerUiModel>) {
        this.brandHighLightList = brandsList
        this.notifyDataSetChanged()
    }

    inner class BrandViewHolder(val binding: BrandHighlightItemRowBinding): RecyclerView.ViewHolder(binding.root) {

        private val viewModel = FrontPageBrandViewModel()

        fun bind(brandRecyclerUiModel: BrandRecyclerUiModel) {
            viewModel.bind(brandRecyclerUiModel)
            binding.viewModel = viewModel

            binding.root.setOnClickListener { itemClickListener.onItemClick(brandRecyclerUiModel.brand) }
        }
    }

    interface BrandItemClickListener {
        fun onItemClick(brand: String)
    }


}