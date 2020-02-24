package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.databinding.CatalogueItemLoadingBinding
import com.buscatumoto.databinding.CatalogueItemRowBinding
import com.buscatumoto.ui.viewmodels.CatalogueItemViewModel
import com.buscatumoto.utils.ui.CatalogueItemClickListener


class CatalogueListAdapter(val catalogueItemClickListener: CatalogueItemClickListener) :
    RecyclerView.Adapter<CatalogueListAdapter.BaseCatalogueViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    private var isLoaderVisible = false

    private var listMoto: MutableList<MotoEntity> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return when (isLoaderVisible) {
            true -> {
                if (position == listMoto.size - 1) {
                    VIEW_TYPE_LOADING
                } else {
                    VIEW_TYPE_NORMAL
                }
            }
            false -> {
                VIEW_TYPE_NORMAL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseCatalogueViewHolder {

        when (viewType) {
            VIEW_TYPE_NORMAL -> {
                return CatalogueViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.catalogue_item_row, parent, false
                    )
                )
            }
            VIEW_TYPE_LOADING -> {

                return ProgressViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.catalogue_item_loading, parent, false
                    )
                )
            }
            else -> {
                return CatalogueViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.catalogue_item_row, parent, false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listMoto.size
    }

    override fun onBindViewHolder(holder: BaseCatalogueViewHolder, position: Int) {

        when (holder) {
            is CatalogueViewHolder -> {
                holder.bind(listMoto[position])
            }
            is ProgressViewHolder -> {

            }
        }
    }

    fun addItems(data: List<MotoEntity>) {
        listMoto.addAll(data)
        notifyDataSetChanged()
    }

    fun updateCatalogue(data: List<MotoEntity>) {
        this.listMoto = data.toMutableList()
        this.notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        listMoto.add(listMoto[0])
        notifyItemInserted(listMoto.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val pos = listMoto.size - 1
        val motoEntity = listMoto[pos]

        if (motoEntity != null) {
            listMoto.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    fun clear() {
        listMoto.clear()
        notifyDataSetChanged()
    }

    open inner class BaseCatalogueViewHolder(private val itemRowBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

    }

    inner class CatalogueViewHolder(private val itemRowBinding: CatalogueItemRowBinding) :
        BaseCatalogueViewHolder(itemRowBinding) {

        val catalogueItemViewModel = CatalogueItemViewModel()

        fun bind(motoEntity: MotoEntity) {
            catalogueItemViewModel.bind(motoEntity)
            catalogueItemViewModel.catalogueItemClickListener = catalogueItemClickListener
            itemRowBinding.viewModel = catalogueItemViewModel
        }
    }

    inner class ProgressViewHolder(private val itemRowBinding: CatalogueItemLoadingBinding) :
        BaseCatalogueViewHolder(itemRowBinding) {


    }

}