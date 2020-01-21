package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.buscatumoto.R
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFormViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener {
        loadFields()
    }

    val brands : MutableLiveData<List<String>> = MutableLiveData()

    init {
        loadFields()
    }

    fun loadFields() {
        //Anonymous class
//        subscription = buscaTuMotoService.getFields().
//            subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onLoadFieldsStart()}
//            .doOnTerminate { onLoadFieldsFinish() }
//            .subscribe( object : Consumer<FieldsResponse> {
//                override fun accept(t: FieldsResponse?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//            }, object : Consumer<Throwable> {
//                override fun accept(t: Throwable?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//            })

        subscription = buscaTuMotoService.getFields().
            subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadFieldsStart()}
            .doOnTerminate { onLoadFieldsFinish() }
            .subscribe({ fieldResponse: FieldsResponse? ->  onLoadFieldsSuccess(fieldResponse)}
            , { onLoadFieldsError() })
    }

//    @CheckReturnValue
//    @SchedulerSupport(SchedulerSupport.NONE)
//    fun subscribe(
//        onNext: Consumer<in T?>?,
//        onError: Consumer<in Throwable?>?
//    ): Disposable {
//        return subscribe(
//            onNext,
//            onError,
//            Functions.EMPTY_ACTION,
//            Functions.emptyConsumer<Disposable>()
//        )
//    }

    private fun onLoadFieldsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onLoadFieldsSuccess(fieldsResponse: FieldsResponse?) {
        brands.value = fieldsResponse?.brandList
    }

    private fun onLoadFieldsError() {
        errorMessage.value = R.string.load_fields_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}