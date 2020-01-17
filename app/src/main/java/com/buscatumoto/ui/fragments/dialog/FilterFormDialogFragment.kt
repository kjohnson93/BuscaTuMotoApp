package com.buscatumoto.ui.fragments.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.data.remote.configuration.APIGatewayResponse
import com.buscatumoto.utils.data.APIConstants
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.utils.ui.FilterFormImpl
import com.buscatumoto.utils.ui.FilterFormMediator

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class FilterFormDialogFragment : DialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): FilterFormDialogFragment {
            return FilterFormDialogFragment()
        }
    }

    var brandSpinner: Spinner? = null
    var modelSpinner: Spinner? = null
    var bikeTypeSpinner: Spinner? = null
    var priceMinSpinner: Spinner? = null
    var priceMaxSpinner: Spinner? = null
    var powerMinSpinner: Spinner? = null
    var powerMaxSpinner: Spinner? = null
    var cilMinSpinner: Spinner? = null
    var cilMaxSpinner: Spinner? = null
    var weightMinSpinner: Spinner? = null
    var weightMaxSpinner: Spinner? = null
    var yearSpinner: Spinner? = null
    var licenseSpinner: Spinner? = null

    var closeIButton: ImageButton? = null
    var refreshIButton: ImageButton? = null
    var acceptIButton: ImageButton? = null

    var filterFormMediator: FilterFormMediator? = null

    var filterFormPgBar: ProgressBar? = null

    private lateinit var subscription: Disposable


    //Fragment is not destroyed. Only View inside Fragment does. fragment.view will be a different object after fragment goes to background etc.
    //Also, every single View that is implemented a View State Saving/Restoring internally (every view as a default)
    //automatically will be saved and  will restore the state. From the view inflated to every component. Causes it to display just perfectly the same as previous.
    //And also don't forget to assign android:id attribute to every single View placed in the layout that you need to enable View State Saving and Restoring
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.MOTOTAG, "onCreateView called")
        var fragmentView = inflater.inflate(R.layout.fragment_filtro_form, container, false)

        bindViews(fragmentView)

        filterFormMediator = FilterFormImpl(
            activity, filterFormPgBar, brandSpinner, modelSpinner, bikeTypeSpinner,
            priceMinSpinner, priceMaxSpinner, powerMinSpinner, powerMaxSpinner,
            cilMinSpinner, cilMaxSpinner, weightMinSpinner, weightMaxSpinner,
            yearSpinner, licenseSpinner, refreshIButton
        )

        attachItemClickListener()

        val buscaTuMotoGateway = BuscaTuMotoApplication.getInstance().buscaTuMotoGateway

        filterFormPgBar?.visibility = View.VISIBLE

        filterFormPgBar?.visibility = View.VISIBLE
        buscaTuMotoGateway?.getFields(object : APIGatewayResponse.SuccessListener<FieldsResponse?> {
            override fun onResponse(response: FieldsResponse?) {
                filterFormPgBar?.visibility = View.GONE
                response?.let {
                    if (response.respuesta == APIConstants.RESPONSE_OK) {
                        fillSpinnerViews(response)
                    }
                }
            }

        }, object : APIGatewayResponse.ErrorListener {
            override fun onError(errorResponse: String?) {
                Log.e(Constants.MOTOTAG, "get fields error: $errorResponse")
                filterFormPgBar?.visibility = View.GONE
            }

        })

        /**
         * First consumer solution:
         */

//        var searchTextObservable = createButtonClickObservable()
//
//        searchTextObservable.subscribe {
//                query -> Toast.makeText(context, "search text toast + ${query}", Toast.LENGTH_LONG).show()
//        }

        //Instead of this, we have to manage threads in order to avoid thread exceptions or blocking the UI.
        /**
         * Thread solution: Use susbcribeOn for observables, and obServeOn for consumers (map and regular consumers) As well
         * as schedulers like Schedulers.io for network requests, Schedulers.computation for event-loops and callbacks,
         * and AndroidSchedulers.mainThread for operating on UI thread
         */


        var searchTextObservable = createButtonClickObservable()

        /**
         * Filter operator
         * filter for filtering events emitted to the consumer
         */
        searchTextObservable.filter {it.length >= 2 } //always trigerring


        /**
         * Debounce operator
         * another type of filter but not based on WHAT but based on WHEN instead.
         * Based on when the event/item was emitted. It waits for a specified amount of time after each item emission for another item.
         * TODO: It should fix the problem of Android's dialog fragment opening more than once when tapping button to open it too quickly.
         */
        searchTextObservable.debounce(1000, TimeUnit.MILLISECONDS)


        /**
         * Merge operator
         * Takes items from two or more observables and puts them into a single observable:
         * TO BE put in practice, it is made to be able to react to multiple observable events at the same time.
         * So it will create and observable that will receive events from two observables.
         */

//        // 1
//        Maybe.create<Boolean> { emitter ->
//            emitter.setCancellable {
//                holder.itemView.imageFavorite.setOnClickListener(null)
//            }
//
//            holder.itemView.imageFavorite.setOnClickListener {
//                emitter.onSuccess((it as CheckableImageView).isChecked) // 2
//            }
//        }.toFlowable().onBackpressureLatest() //
//            .observeOn(Schedulers.io())
//            .map { isChecked ->
//                cheese.favorite = if (!isChecked) 1 else 0
//                val database = CheeseDatabase.getInstance(holder.itemView.context).cheeseDao()
//                database.favoriteCheese(cheese) // 4
//                cheese.favorite // 5
//            }
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                holder.itemView.imageFavorite.isChecked = it == 1 // 6
//            }

       // https://www.raywenderlich.com/2071847-reactive-programming-with-rxandroid-in-kotlin-an-introduction
        //https://proandroiddev.com/mvvm-with-kotlin-android-architecture-components-dagger-2-retrofit-and-rxandroid-1a4ebb38c699


         searchTextObservable
            .subscribeOn(AndroidSchedulers.mainThread()) //In Android, observables that emits events from UI should execute on the main thread.
            .observeOn(Schedulers.io()) //Specify that next operator should be called on the I/O thread.
            .map { query -> Toast.makeText(context, "MAP search text toast + ${query}", Toast.LENGTH_LONG).show()} //Simulating a Map operation (Aditional operation before passign to consumer)
            .observeOn(AndroidSchedulers.mainThread()) //After we are done, we pass the result back to work on the main thread
            .subscribe { //This way, if map operation is asynchronous, UI will not be blocked and UI should be responsive even when there is a task in progress.
                query -> Toast.makeText(context, "Subscribedsearch text toast + ${query}", Toast.LENGTH_LONG).show()
            }





        return fragmentView
    }

    private fun fillSpinnerViews(response: FieldsResponse) {

        //brand
        var brandArrayList: ArrayList<String> = ArrayList()
        brandArrayList.addAll(response.brandList)
        brandArrayList.add(0, "-Marca-")
        val brandSpinnerAdapter = ArrayAdapter<String>(
            context, android.R.layout.simple_spinner_item,
            brandArrayList
        )
        brandSpinner?.adapter = brandSpinnerAdapter
        brandSpinner?.setSelection(0)

        //modelo placeholder
        val modelList: ArrayList<String> = ArrayList()
        modelList.add(0, "Elegir marca")
        val modelSpinnerAdapter = ArrayAdapter<String>(
            context, android.R.layout.simple_spinner_item,
            modelList
        )
        modelSpinner?.adapter = modelSpinnerAdapter
        modelSpinner?.setSelection(0)

        //bike type
        val bikeTypeList: ArrayList<String> = ArrayList()
        bikeTypeList.addAll(response.bikeTypesList)
        bikeTypeList.removeAt(0)
        bikeTypeList.add(0, "-Tipo de moto-")
        val bikeTypeSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bikeTypeList)
        bikeTypeSpinner?.adapter = bikeTypeSpinnerAdapter
        bikeTypeSpinner?.setSelection(0)

        //price min
        val priceMinList: ArrayList<String> = ArrayList()
        priceMinList.addAll(response.priceMinList as ArrayList<String>)
        priceMinList.add(0, "-Precio desde-")
        val priceMinSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMinList)
        priceMinSpinner?.adapter = priceMinSpinnerAdapter
        priceMinSpinner?.setSelection(0)

        //price max
        val priceMaxList: ArrayList<String> = ArrayList()
        priceMaxList.addAll(response.priceMaxList as ArrayList<String>)
        priceMaxList.add(0, "-Precio hasta-")
        val priceMaxSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priceMaxList)
        priceMaxSpinner?.adapter = priceMaxSpinnerAdapter
        priceMaxSpinner?.setSelection(0)

        //power min
        val powerMinList: ArrayList<String> = ArrayList()
        powerMinList.addAll(response.powerMinList as ArrayList<String>)
        powerMinList.add(0, "-Potencia desde-")
        val powerMinSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMinList)
        powerMinSpinner?.adapter = powerMinSpinnerAdapter
        powerMinSpinner?.setSelection(0)

        //power max
        val powerMaxList: ArrayList<String> = ArrayList()
        powerMaxList.addAll(response.powerMaxList as ArrayList<String>)
        powerMaxList.add(0, "-Potencia hasta-")
        val powerMaxSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, powerMaxList)
        powerMaxSpinner?.adapter = powerMaxSpinnerAdapter
        powerMaxSpinner?.setSelection(0)

        //cil min
        val cilMinList: ArrayList<String> = ArrayList()
        cilMinList.addAll(response.cilMinList as ArrayList<String>)
        cilMinList.add(0, "-Cilindrada desde-")
        val cilMinSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMinList)
        cilMinSpinner?.adapter = cilMinSpinnerAdapter
        cilMinSpinner?.setSelection(0)

        //cil max
        val cilMaxList: ArrayList<String> = ArrayList()
        cilMaxList.addAll(response.cilMaxList as ArrayList<String>)
        cilMaxList.add(0, "-Cilindrada hasta-")
        val cilMaxSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cilMaxList)
        cilMaxSpinner?.adapter = cilMaxSpinnerAdapter
        cilMaxSpinner?.setSelection(0)

        //weight min
        val weightMinList: ArrayList<String> = ArrayList()
        weightMinList.addAll(response.weightMinList as ArrayList<String>)
        weightMinList.add(0, "-Peso desde-")
        val weightMinSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMinList)
        weightMinSpinner?.adapter = weightMinSpinnerAdapter
        weightMinSpinner?.setSelection(0)

        //weight max
        val weightMaxList: ArrayList<String> = ArrayList()
        weightMinList.addAll(response.weightMaxList as ArrayList<String>)
        weightMaxList.add(0, "-Peso hasta-")
        val weightMaxSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weightMaxList)
        weightMaxSpinner?.adapter = weightMaxSpinnerAdapter
        weightMaxSpinner?.setSelection(0)

        //year
        val yearList: ArrayList<String> = ArrayList()
        yearList.addAll(response.yearList as ArrayList<String>)
        yearList.add(0, "-Año-")
        val yearSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, yearList)
        yearSpinner?.adapter = yearSpinnerAdapter
        yearSpinner?.setSelection(0)

        //license
        val licenseTypeList: ArrayList<String> = ArrayList()
        licenseTypeList.addAll(response.licenses as ArrayList<String>)
        licenseTypeList.add(0, "-Permiso-")
        val licenseSpinnerAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, licenseTypeList)
        licenseSpinner?.adapter = licenseSpinnerAdapter
        licenseSpinner?.setSelection(0)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.MOTOTAG, "onActivityCreated called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constants.MOTOTAG, "onStart called")
    }

    private fun bindViews(fragmentView: View) {
        brandSpinner = fragmentView.findViewById(R.id.brandSpinner)
        modelSpinner = fragmentView.findViewById(R.id.modelSpinner)
        bikeTypeSpinner = fragmentView.findViewById(R.id.bikeTypeSpinner)
        priceMinSpinner = fragmentView.findViewById(R.id.priceMinSpinner)
        priceMaxSpinner = fragmentView.findViewById(R.id.priceMaxSpinner)
        powerMinSpinner = fragmentView.findViewById(R.id.powerMinSpinner)
        powerMaxSpinner = fragmentView.findViewById(R.id.powerMaxSpinner)
        cilMinSpinner = fragmentView.findViewById(R.id.cilMinSpinner)
        cilMaxSpinner = fragmentView.findViewById(R.id.cilMaxSpinner)
        weightMinSpinner = fragmentView.findViewById(R.id.weightMinSpinner)
        weightMaxSpinner = fragmentView.findViewById(R.id.weightMaxSpinner)
        yearSpinner = fragmentView.findViewById(R.id.yearSpinner)
        licenseSpinner = fragmentView.findViewById(R.id.licenseSpinner)

        closeIButton = fragmentView.findViewById(R.id.filtrar_close_ibtn)
        refreshIButton = fragmentView.findViewById(R.id.filtrar_refresh_ibtn)
        acceptIButton = fragmentView.findViewById(R.id.filtrar_accept_ibtn)

//        filterFormPgBar = fragmentView.findViewById(R.id.filter_dialog_progressBar)

        closeIButton?.setOnClickListener(this)
//        refreshIButton?.setOnClickListener(this)
        acceptIButton?.setOnClickListener(this)

    }

    private fun attachItemClickListener() {
        brandSpinner?.onItemSelectedListener =
            filterFormMediator as AdapterView.OnItemSelectedListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //Dialog fragment bug: In order to make setSelection work on a restore instance case, we have to set animation to false.
//        powerMaxSpinner?.setSelection(0, false)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.filtrar_close_ibtn -> {
                dismiss()
            }
//            R.id.filtrar_refresh_ibtn -> {
//                filterFormMediator?.notify(refreshIButton?.id, 0)
//            }
            R.id.filtrar_accept_ibtn -> {
                //API request and then navigate.
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun createButtonClickObservable(): Observable<String> {

//        return Observable.create { emitter ->
//            refreshIButton?.setOnClickListener { emitter.onNext("Refresh!") }
//        }
        return Observable.create { emitter ->
            refreshIButton?.setOnClickListener { emitter.onNext("Refresh") }
            emitter.setCancellable {
                refreshIButton?.setOnClickListener(null)
            }
        }
    }
}