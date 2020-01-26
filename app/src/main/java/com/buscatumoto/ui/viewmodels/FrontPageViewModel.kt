package com.buscatumoto.ui.viewmodels

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    val searchBrandsAdapter = SearchBrandsRecyclerAdapter()

    init {
        loadBrands()
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val typedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)

        val drawableArrayList = ArrayList<Drawable?>()
        val drawableList : List<Drawable>


        var index = 0

        while (index < typedArray.length()) {
            drawableArrayList.add(typedArray.getDrawable(index))

//            drawableList[index] = typedArray.getDrawable(index)

            index ++
        }

//      private val list: List<ItemInfo> = listOf(itemList.last()) + itemList + listOf(itemList.first())
        val modifiedList: List<Drawable?> = listOf(drawableArrayList.last()) + drawableArrayList + listOf(drawableArrayList.first())

        searchBrandsAdapter.updateBrandHighLights(modifiedList as List<Drawable>)
    }


}