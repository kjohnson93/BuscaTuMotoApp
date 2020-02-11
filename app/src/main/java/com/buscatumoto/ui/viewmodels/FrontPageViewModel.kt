package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    private var brandSelected = MutableLiveData<String>()
    fun getbrandSelected(): MutableLiveData<String> = brandSelected

    val searchBrandsAdapter = SearchBrandsRecyclerAdapter(object: SearchBrandsRecyclerAdapter.BrandItemClickListener {
        override fun onItemClick(brand: String) {
            brandSelected.value = brand
        }
    })

    init {
        loadBrands()
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val brandNamesTypedArray = context.resources.obtainTypedArray(R.array.brand_names_array)

        val drawableArrayList = ArrayList<Drawable?>()

        val  brandRecyclerUiModelList = ArrayList<BrandRecyclerUiModel?>()

        var index = 0

        while (index < drawabletypedArray.length()) {
//            drawableArrayList.add(typedArray.getDrawable(index))
            val brandRecyclerUiModel = BrandRecyclerUiModel(brandNamesTypedArray.getString(index), drawabletypedArray.getDrawable(index))
            brandRecyclerUiModelList.add(brandRecyclerUiModel)
            index ++
        }

        val modifiedList: List<BrandRecyclerUiModel?> = listOf(brandRecyclerUiModelList.last()) + brandRecyclerUiModelList + listOf(brandRecyclerUiModelList.first())

        searchBrandsAdapter.updateBrandHighLights(modifiedList as List<BrandRecyclerUiModel>)
    }


}