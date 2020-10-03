package com.buscatumoto.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailNoValidColBinding
import com.buscatumoto.databinding.DetailSpecsItemRowBinding
import com.buscatumoto.databinding.DetailSpecsOneColBinding
import com.buscatumoto.databinding.DetailSpecsTwoColBinding
import com.buscatumoto.ui.viewmodels.DetailSpecItemViewModel

class DetailSpecsRecyclerAdapter(): RecyclerView.Adapter<DetailSpecsRecyclerAdapter.DetailSpecsViewHolder> () {

    companion object {
        val ONE_COLUMN_VIEW_TYPE = 1
        val TWO_COLUM_VIEW_TYPE = 2
        val NO_VALID_VIEW_TYPE = 0
    }
    private var listData = ArrayList<ArrayList<String>> ()
    private var viewColType = NO_VALID_VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSpecsViewHolder {

        return when (viewColType) {
            ONE_COLUMN_VIEW_TYPE -> {
                val itemBinding: DetailSpecsOneColBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.detail_specs_one_col,
                    parent,
                    false
                )
                return DetailSpecsViewHolderOneCol(itemBinding)
            }
            TWO_COLUM_VIEW_TYPE -> {
                val itemBinding: DetailSpecsTwoColBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.detail_specs_two_col,
                    parent,
                    false
                )
                return DetailSpecsViewHolderTwoCol(itemBinding)
            }
            else -> {
                val itemBinding: DetailNoValidColBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.detail_no_valid_col,
                    parent,
                    false
                )
                return DetailSpecsViewHolderNoValidCol(itemBinding)
            }
        }

    }

    override fun getItemCount(): Int =
         when (this.viewColType) {
            ONE_COLUMN_VIEW_TYPE -> {
                listData[0].size
            }
            TWO_COLUM_VIEW_TYPE -> {
                listData[0].size
            }
            else -> {
                1
            }
         }

    override fun onBindViewHolder(holder: DetailSpecsViewHolder, position: Int) {

        when (holder) {
            is DetailSpecsViewHolderOneCol -> {
//                //pass matrix to arraylist
//                val uniqueList = ArrayList<String>()
//
//                //Remove empty arrays
//                for (element in listData) {c
//                    element.remove("")
//
//                    if (element.isEmpty()) {
//                        listData.remove(element)
//                    }
//                }
//
//                for (element in listData) {
//                    for (subElement in element) {
//                        uniqueList.add(subElement)
//                    }
//                }
//
//                holder.bind(uniqueList[position])
            }
            is DetailSpecsViewHolderTwoCol -> {
//                //pass matrix to arraylist
//                val uniqueList = ArrayList<String>()
//
//                //Remove empty arrays
//                for (element in listData) {
//                    element.remove("")
//
//                    if (element.isEmpty()) {
//                        listData.remove(element)
//                    }
//                }
//
//                for (element in listData) {
//                    for (subElement in element) {
//                        uniqueList.add(subElement)
//                    }
//                }
//
//                holder.bind(uniqueList[position])
            }
            is DetailSpecsViewHolderNoValidCol -> {

            }
        }


    }

    fun updateData(data:  ArrayList<ArrayList<String>>) {
        this.listData = data

        viewColType = when (listData.size) {
            ONE_COLUMN_VIEW_TYPE -> {
                ONE_COLUMN_VIEW_TYPE
            }
            TWO_COLUM_VIEW_TYPE -> {
                TWO_COLUM_VIEW_TYPE
            }
            else -> {
                NO_VALID_VIEW_TYPE
            }

        }

        this.notifyDataSetChanged()
    }

    open class DetailSpecsViewHolder (itemView: ViewDataBinding): RecyclerView.ViewHolder(itemView.root)

    inner class DetailSpecsViewHolderOneCol(itemView: DetailSpecsOneColBinding) :
        DetailSpecsViewHolder(itemView) {

        private var detailSpecItemViewModel = DetailSpecItemViewModel()

        fun bind(text: String) {
            detailSpecItemViewModel.bind(text)
        }
    }

    inner class DetailSpecsViewHolderTwoCol(itemView: DetailSpecsTwoColBinding) :
        DetailSpecsViewHolder(itemView) {

        private var detailSpecItemViewModel = DetailSpecItemViewModel()

        fun bind(text: String) {
            detailSpecItemViewModel.bind(text)
        }
    }

    inner class DetailSpecsViewHolderNoValidCol(itemView: DetailNoValidColBinding) :
        DetailSpecsViewHolder(itemView) {

        private var detailSpecItemViewModel = DetailSpecItemViewModel()

        fun bind(text: String) {
            detailSpecItemViewModel.bind(text)
        }
    }
}
