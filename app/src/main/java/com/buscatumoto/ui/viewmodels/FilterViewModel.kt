package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.domain.features.search.GetFieldsUseCase
import com.buscatumoto.ui.adapters.FilterRecyclerAdapter
import com.buscatumoto.ui.adapters.FilterRecyclerItem
import com.buscatumoto.utils.global.removeEmptyValues
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class FilterViewModel @Inject constructor(private val getFieldsUseCase: GetFieldsUseCase) : BaseViewModel() {

    //Visibilities
    val loadingVisibility = MutableLiveData<Int> ()
    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()
    val minPriceExpanded = MutableLiveData<Boolean> ()
    val maxPriceExpanded = MutableLiveData<Boolean> ()
    val minPowerExpanded = MutableLiveData<Boolean> ()
    val maxPowerExpanded = MutableLiveData<Boolean> ()
    val minDisplacementExpanded = MutableLiveData<Boolean> ()
    val maxDisplacementExpanded = MutableLiveData<Boolean> ()
    val minWeightExpanded = MutableLiveData<Boolean> ()
    val maxWeightExpanded = MutableLiveData<Boolean> ()
    val yearExpanded = MutableLiveData<Boolean> ()
    val licenseExpanded = MutableLiveData<Boolean> ()

    //Adapters
    var brandRecyclerAdapter = FilterRecyclerAdapter()
    var bikeTypeRecyclerAdapter = FilterRecyclerAdapter()
    var minPriceRecyclerAdapter = FilterRecyclerAdapter()
    var maxPriceRecyclerAdapter = FilterRecyclerAdapter()
    var minPowerRecyclerAdapter = FilterRecyclerAdapter()
    var maxPowerRecyclerAdapter = FilterRecyclerAdapter()
    var minDisplacementRecyclerAdapter = FilterRecyclerAdapter()
    var maxDisplacementRecyclerAdapter = FilterRecyclerAdapter()
    var minWeightRecyclerAdapter = FilterRecyclerAdapter()
    var maxWeightRecyclerAdapter = FilterRecyclerAdapter()
    var yearRecyclerAdapter = FilterRecyclerAdapter()
    var licenseRecyclerAdapter = FilterRecyclerAdapter()

    //Mutables
    val brandSelectedMutable = MutableLiveData<String> ()
    val bikeTypeSelectedMutable = MutableLiveData<String> ()
    val minPriceSelectedMutable = MutableLiveData<String> ()
    val maxPriceSelectedMutable = MutableLiveData<String> ()
    val minPowerSelectedMutable = MutableLiveData<String> ()
    val maxPowerSelectedMutable = MutableLiveData<String> ()
    val minDisplacementSelectedMutable = MutableLiveData<String> ()
    val maxDisplacementSelectedMutable = MutableLiveData<String> ()
    val minWeightSelectedMutable = MutableLiveData<String> ()
    val maxWeightSelectedMutable = MutableLiveData<String> ()
    val yearSelectedMutable = MutableLiveData<String> ()
    val licenseSelectedMutable = MutableLiveData<String> ()

    //Utils
    private lateinit var lastBrandSelected: String

    //Error retry management
    public var errorMutable = MutableLiveData<RetryErrorModel>()
    private val errorClickListener = View.OnClickListener {
        when (errorMutable.value?.requestType) {
            RetryErrorModel.FIELDS_ERROR -> {
                loadFilterData()
            }
        }
    }

    fun getErrorClickListener() : View.OnClickListener = errorClickListener

    //Observers
    private val brandObserver = Observer<String> {
        brandSelectedMutable.value = it
        brandExpanded.value = false
    }
    private val bikeTypeObserver = Observer<String> {
        bikeTypeSelectedMutable.value = it
        bikeTypeExpanded.value = false
    }
    private val minPriceObserver = Observer<String> {
        minPriceSelectedMutable.value = it
        minPriceExpanded.value = false
    }
    private val maxPriceObserver = Observer<String> {
        maxPriceSelectedMutable.value = it
        maxPriceExpanded.value = false
    }
    private val minPowerObserver = Observer<String> {
        minPowerSelectedMutable.value = it
        minPowerExpanded.value = false
    }
    private val maxPowerObserver = Observer<String> {
        maxPowerSelectedMutable.value = it
        maxPowerExpanded.value = false
    }
    private val minDisplacementObserver = Observer<String> {
        minDisplacementSelectedMutable.value = it
        minDisplacementExpanded.value = false
    }
    private val maxDisplacementObserver = Observer<String> {
        maxDisplacementSelectedMutable.value = it
        maxDisplacementExpanded.value = false
    }
    private val minWeightObserver = Observer<String> {
        minWeightSelectedMutable.value = it
        minWeightExpanded.value = false
    }
    private val maxWeightObserver = Observer<String> {
        maxWeightSelectedMutable.value = it
        maxWeightExpanded.value = false
    }
    private val yearObserver = Observer<String> {
        yearSelectedMutable.value = it
        yearExpanded.value = false
    }
    private val licenseObserver = Observer<String> {
        licenseSelectedMutable.value = it
        licenseExpanded.value = false
    }

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false
        minPriceExpanded.value = false
        maxPriceExpanded.value = false
        minPowerExpanded.value = false
        maxPowerExpanded.value = false
        minDisplacementExpanded.value = false
        maxDisplacementExpanded.value = false
        minWeightExpanded.value = false
        maxWeightExpanded.value = false
        yearExpanded.value = false
        licenseExpanded.value = false

        loadFilterData()
        observeSelectedValues()
    }

    override fun onCleared() {
        super.onCleared()
        brandRecyclerAdapter.itemNameSelectedMutable.removeObserver(brandObserver)
        bikeTypeRecyclerAdapter.itemNameSelectedMutable.removeObserver(bikeTypeObserver)
        minPriceRecyclerAdapter.itemNameSelectedMutable.removeObserver(minPriceObserver)
        maxPriceRecyclerAdapter.itemNameSelectedMutable.removeObserver(maxPriceObserver)
        minPowerRecyclerAdapter.itemNameSelectedMutable.removeObserver(minPowerObserver)
        maxPowerRecyclerAdapter.itemNameSelectedMutable.removeObserver(maxPowerObserver)
        minDisplacementRecyclerAdapter.itemNameSelectedMutable.removeObserver(minDisplacementObserver)
        maxDisplacementRecyclerAdapter.itemNameSelectedMutable.removeObserver(maxDisplacementObserver)
        minWeightRecyclerAdapter.itemNameSelectedMutable.removeObserver(minWeightObserver)
        maxWeightRecyclerAdapter.itemNameSelectedMutable.removeObserver(maxWeightObserver)
        maxDisplacementRecyclerAdapter.itemNameSelectedMutable.removeObserver(maxWeightObserver)
        yearRecyclerAdapter.itemNameSelectedMutable.removeObserver(yearObserver)
        licenseRecyclerAdapter.itemNameSelectedMutable.removeObserver(licenseObserver)
    }

    private fun observeSelectedValues() {
        brandRecyclerAdapter.itemNameSelectedMutable.observeForever(brandObserver)
        bikeTypeRecyclerAdapter.itemNameSelectedMutable.observeForever(bikeTypeObserver)
        minPriceRecyclerAdapter.itemNameSelectedMutable.observeForever(minPriceObserver)
        maxPriceRecyclerAdapter.itemNameSelectedMutable.observeForever(maxPriceObserver)
        minPowerRecyclerAdapter.itemNameSelectedMutable.observeForever(minPowerObserver)
        maxPowerRecyclerAdapter.itemNameSelectedMutable.observeForever(maxPowerObserver)
        minDisplacementRecyclerAdapter.itemNameSelectedMutable.observeForever(minDisplacementObserver)
        maxDisplacementRecyclerAdapter.itemNameSelectedMutable.observeForever(maxDisplacementObserver)
        minWeightRecyclerAdapter.itemNameSelectedMutable.observeForever(minWeightObserver)
        maxWeightRecyclerAdapter.itemNameSelectedMutable.observeForever(maxWeightObserver)
        maxDisplacementRecyclerAdapter.itemNameSelectedMutable.observeForever(maxWeightObserver)
        yearRecyclerAdapter.itemNameSelectedMutable.observeForever(yearObserver)
        licenseRecyclerAdapter.itemNameSelectedMutable.observeForever(licenseObserver)
    }

    private fun loadFilterData() {
        viewModelScope.launch(Dispatchers.IO) {
            val fieldLiveData =  getFieldsUseCase.execute()

            withContext(Dispatchers.Main) {
                fieldLiveData.observeForever(Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            loadingVisibility.value = View.GONE
                            result.data?.brandList?.let { loadBrandAdapter(it) }
                            result.data?.bikeTypesList?.let { loadBikeTypeAdapter(it) }
                            result.data?.priceMinList?.let {
                                val listFormatted: List<String> = formatIntListToStringList(it)
                                loadMinPriceAdapter(listFormatted)
                            }
                            result.data?.priceMaxList?.let {
                                val listFormatted: List<String> = formatIntListToStringList(it)
                                loadMaxPriceAdapter(listFormatted)
                            }
                            result.data?.powerMinList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMinPowerAdapter(listFormatted)
                            }
                            result.data?.powerMaxList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMaxPowerAdapter(listFormatted)
                            }
                            result.data?.cilMinList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMinDisplacementAdapter(listFormatted)
                            }
                            result.data?.cilMaxList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMaxDisplacementAdapter(listFormatted)
                            }
                            result.data?.weightMinList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMinWeightAdapter(listFormatted)
                            }
                            result.data?.weightMaxList?.let {
                                val listFormatted: List<String> = formatFloatListToStringList(it)
                                loadMaxWeightAdapter(listFormatted)
                            }
                            result.data?.yearList?.let {
                                val listFormatted: List<String> = formatIntListToStringList(it)
                                loadYearAdapter(listFormatted)
                            }
                            result.data?.licenses?.let {
                                loadLicenseAdapter(it)
                            }
                            fieldLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                        Result.Status.LOADING -> {
                            onLoading()
                        }
                        Result.Status.ERROR ->{
                            onLoadFieldsError(result.message)
                            fieldLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                    }
                })
            }
        }
    }

    private fun formatIntListToStringList(it: List<Int>) = it.map { it.toString() }

    private fun formatFloatListToStringList(it: List<Float>) = it.map { it.toString() }



    private fun loadBrandAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

            var index = 0
            while (index < smallerSize) {
                val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
                filterItemList.add(filterRecyclerItem)
                index ++
            }
            brandRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadBikeTypeAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val filterItemList = ArrayList<FilterRecyclerItem>()
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.bike_types_array)

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (listData.size <= drawabletypedArray.length()) {
            listData.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */


            var index = 0
            while (index < smallerSize) {
                val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
                filterItemList.add(filterRecyclerItem)
                index ++
            }
            bikeTypeRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMinPriceAdapter(listData: List<String>) {
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray =
            context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (listData.size <= drawabletypedArray.length()) {
            listData.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(listData[index],
                drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        minPriceRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMaxPriceAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        maxPriceRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMinPowerAdapter(listData: List<String>) {
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray =
            context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (listData.size <= drawabletypedArray.length()) {
            listData.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(listData[index],
                drawabletypedArray.getDrawable(index))
            filterList.add(filterRecyclerItem)
            index ++
        }
        minPowerRecyclerAdapter.updateFilterItemsList(filterList.toList())
    }

    private fun loadMaxPowerAdapter(listData: List<String>) {
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray =
            context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (listData.size <= drawabletypedArray.length()) {
            listData.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(listData[index],
                drawabletypedArray.getDrawable(index))
            filterList.add(filterRecyclerItem)
            index ++
        }
        maxPowerRecyclerAdapter.updateFilterItemsList(filterList.toList())
    }

    private fun loadMinDisplacementAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        minDisplacementRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMaxDisplacementAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        maxDisplacementRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMinWeightAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        minWeightRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadMaxWeightAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        maxWeightRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadYearAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        yearRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    private fun loadLicenseAdapter(listData: List<String>) {
        var mutableList = listData as ArrayList
        //Using iterator to avoid ConcurrentModificationException
        mutableList = removeEmptyValues(mutableList)

        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val  filterItemList = ArrayList<FilterRecyclerItem>()

        /**
         * Size workaround until images are hosted online and not locally.
         */
        var smallerSize = 0
        smallerSize = if (mutableList.size <= drawabletypedArray.length()) {
            mutableList.size
        } else {
            drawabletypedArray.length()
        }
        /**
         * Size workaround until images are hosted online and not locally.
         */

        var index = 0
        while (index < smallerSize) {
            val filterRecyclerItem = FilterRecyclerItem(mutableList[index], drawabletypedArray.getDrawable(index))
            filterItemList.add(filterRecyclerItem)
            index ++
        }
        licenseRecyclerAdapter.updateFilterItemsList(filterItemList.toList())
    }

    fun onBrandLayoutClick() {
        brandExpanded.value = brandExpanded.value?.not()
    }

    fun onBikeTypeLayoutClick() {
        bikeTypeExpanded.value = bikeTypeExpanded.value?.not()
    }

    fun onMinPriceLayoutClick() {
        minPriceExpanded.value = minPriceExpanded.value?.not()
    }

    fun onMaxPriceLayoutClick() {
        maxPriceExpanded.value = maxPriceExpanded.value?.not()
    }

    fun onMinPowerLayoutClick() {
        minPowerExpanded.value = minPowerExpanded.value?.not()
    }

    fun onMaxPowerLayoutClick() {
        maxPowerExpanded.value = maxPowerExpanded.value?.not()
    }

    fun onMinDisplacementLayoutClick() {
        minDisplacementExpanded.value = minDisplacementExpanded.value?.not()
    }

    fun onMaxDisplacementLayoutClick() {
        maxDisplacementExpanded.value = maxDisplacementExpanded.value?.not()
    }

    fun onMinWeightLayoutClick() {
        minWeightExpanded.value = minWeightExpanded.value?.not()
    }

    fun onMaxWeightLayoutClick() {
        maxWeightExpanded.value = maxWeightExpanded.value?.not()
    }

    fun onYearLayoutClick() {
        yearExpanded.value = yearExpanded.value?.not()
    }

    fun onLicenseLayoutClick() {
        licenseExpanded.value = licenseExpanded.value?.not()
    }

    private fun onLoading() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsError(message: String?) {
        loadingVisibility.value = View.GONE
        errorMutable.value = RetryErrorModel(R.string.load_fields_error, RetryErrorModel.FIELDS_ERROR)
    }




}