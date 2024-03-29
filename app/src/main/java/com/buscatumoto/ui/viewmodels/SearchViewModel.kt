package com.buscatumoto.ui.viewmodels

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuildConfig
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.domain.features.search.SearchUseCase
import com.buscatumoto.ui.activities.SearchActivity
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

class SearchViewModel @Inject constructor
    (private val buscaTuMotoRepository: BuscaTuMotoRepository,
    private val searchUseCase: SearchUseCase) : BaseViewModel() {

    //FIXME this line should be deleted and search a way that this vm does not know a view
    //Should communicate using only observables
    lateinit var lifeCycleOwner: SearchActivity
    lateinit var screenNavigator: ScreenNavigator
    private lateinit var retryErrorModel: RetryErrorModel
    private var errorModel = MutableLiveData<RetryErrorModel>()
    fun getError() = errorModel
    private lateinit var lastSearch: String
    val searchTextMutable = MutableLiveData<String> ()
    val versionNameMutable = MutableLiveData<String>()
    val navigateMutable = MutableLiveData<Boolean>()

    fun getErrorClickListener() : View.OnClickListener = retryErrorClickListener
    private val retryErrorClickListener = View.OnClickListener {
                navigateBySearch(lastSearch)
    }

    init {
        loadVersionOnDrawerLayout()
    }

    fun loadVersionOnDrawerLayout() {
        val builder = StringBuilder()
        builder.append(BuscaTuMotoApplication.getInstance().getString(R.string.version))
        builder.append(" ").append(BuildConfig.VERSION_NAME)
        versionNameMutable.value = builder.toString()
    }

    val loadingVisibility = MutableLiveData<Int>().apply {
        this.value = View.GONE
    }

    fun navigateBySearch(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchUseCase.insertSearch(search)
            navigateMutable.postValue(true)
        }
    }

    fun onSearchRequested(search: String) {
        Timber.d("Search requested: $search")
        lastSearch = search
        searchTextMutable.value = search
        navigateBySearch(search)
    }
}