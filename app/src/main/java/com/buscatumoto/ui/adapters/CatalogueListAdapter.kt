package com.buscatumoto.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.get
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.databinding.CatalogueItemLoadingBinding
import com.buscatumoto.databinding.CatalogueItemRowBinding
import com.buscatumoto.domain.features.catalogue.GetModelImageUseCase
import com.buscatumoto.ui.viewmodels.CatalogueItemViewModel
import com.buscatumoto.utils.global.DISPLACEMENT_UNKNOWN
import com.buscatumoto.utils.global.POWER_UNKNOWN
import com.buscatumoto.utils.global.PRICE_UNKNOWN
import com.buscatumoto.utils.global.WEIGHT_UNKNOWN
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking


class CatalogueListAdapter(
    val catalogueItemClickListener: CatalogueItemClickListener,
    val viewLifecycleOwner: LifecycleOwner,
    val context: Context
) :
    RecyclerView.Adapter<CatalogueListAdapter.BaseCatalogueViewHolder>(){

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

        var pos = 0

        if (listMoto.size == 0 ) {
            pos = 0
        } else {
            pos = listMoto.size - 1
        }
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
        BaseCatalogueViewHolder(itemRowBinding), CoroutineScope by MainScope() {

        private val catalogueItemViewModel = CatalogueItemViewModel(GetModelImageUseCase())

        fun bind(motoEntity: MotoEntity) {
            catalogueItemViewModel.bind(motoEntity)
            itemRowBinding.root.setOnClickListener { catalogueItemClickListener.onItemClick(
                motoEntity.id
            ) }
            itemRowBinding.viewModel = catalogueItemViewModel

            /**
             * Observer section
             */
            catalogueItemViewModel.modelDisplacementMutable.observe(
                viewLifecycleOwner, Observer {
                    itemRowBinding.highlightDisplacement.text = parseDisplacement(it)
                }
            )
            catalogueItemViewModel.modelWeightMutable.observe(
                viewLifecycleOwner, Observer {
                    itemRowBinding.highlightWeight.text = parseWeight(it)
                }
            )
            catalogueItemViewModel.modelPowerMutable.observe(
                viewLifecycleOwner, Observer {
                    itemRowBinding.highlightPower.text = parsePower(it)
                }
            )
            catalogueItemViewModel.modelPriceMutable.observe(
                viewLifecycleOwner, Observer {
                    itemRowBinding.highlightPrice.text = parsePrice(it)
                }
            )
            /**
             * Observer section
             */
        }

        private fun parseDisplacement(value: Double): String? {
            return if (value == DISPLACEMENT_UNKNOWN) {
                context.resources.getString(R.string.displacement_unknown)
            } else {
                context.resources.getString(R.string.highlight_displacement).format(value.toString())
            }
        }

        private fun parseWeight(value: Double): String? {
            return if (value == WEIGHT_UNKNOWN) {
                context.resources.getString(R.string.weight_unknown)
            } else {
                context.resources.getString(R.string.highlight_weight).format(value.toString())
            }
        }

        private fun parsePower(value: Int): String? {
            return if (value == POWER_UNKNOWN) {
                context.resources.getString(R.string.power_unknown)
            } else {
                context.resources.getString(R.string.highlight_weight).format(value.toString())
            }
        }

        private fun parsePrice(value: Int): String? {
            return if (value == PRICE_UNKNOWN) {
                context.resources.getString(R.string.price_unknown)
            } else {
                val string = context.resources.getString(R.string.highlight_price)
                string.format(value.toString())
            }
        }
    }

    inner class ProgressViewHolder(private val itemRowBinding: CatalogueItemLoadingBinding) :
        BaseCatalogueViewHolder(itemRowBinding) {


    }

}