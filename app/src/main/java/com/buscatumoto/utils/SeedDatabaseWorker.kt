package com.buscatumoto.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.buscatumoto.data.local.AppDatabase
import com.buscatumoto.data.local.Search
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            try {
                applicationContext.assets.open("BuscaTuMotoDB").use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<Search>>() {}.type
                        val list: List<Search> = Gson().fromJson(jsonReader, type)

                        AppDatabase.getInstance(applicationContext).searchDao().insertAll(list)

                        Result.success()
                    }
                }
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}