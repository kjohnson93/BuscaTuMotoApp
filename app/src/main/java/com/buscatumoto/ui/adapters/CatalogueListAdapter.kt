package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.databinding.CatalogueItemRowBinding
import com.buscatumoto.ui.viewmodels.CatalogueItemViewModel
import com.buscatumoto.utils.ui.CatalogueItemClickListener

class CatalogueListAdapter(val catalogueItemClickListener: CatalogueItemClickListener): RecyclerView.Adapter<CatalogueListAdapter.CatalogueViewHolder>() {

    private var listMoto: List<MotoEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogueViewHolder {
        val binding: CatalogueItemRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.catalogue_item_row, parent, false)

        return CatalogueViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listMoto.size
    }

    override fun onBindViewHolder(holder: CatalogueViewHolder, position: Int) {
        holder.bind(listMoto[position])

    }

    fun updateCatalogue(data: List<MotoEntity>) {
        this.listMoto = data
        this.notifyDataSetChanged()
    }

    inner class CatalogueViewHolder(private val itemRowBinding: CatalogueItemRowBinding): RecyclerView.ViewHolder(itemRowBinding.root) {

        val catalogueItemViewModel = CatalogueItemViewModel()

        fun bind(motoEntity: MotoEntity) {
            catalogueItemViewModel.bind(motoEntity)
            catalogueItemViewModel.catalogueItemClickListener = catalogueItemClickListener
            itemRowBinding.viewModel = catalogueItemViewModel
        }
    }

}