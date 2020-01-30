package com.buscatumoto.data.local

import android.content.Context
import android.databinding.adapters.Converters
import com.buscatumoto.utils.SeedDatabaseWorker


/**
 * The Room database for this app
 */
//@Database(entities = [Search::class],
//    version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun searchDao(): SearchDao


//    https://proandroiddev.com/android-architecture-starring-kotlin-coroutines-jetpack-mvvm-room-paging-retrofit-and-dagger-7749b2bae5f7
//    https://github.com/Eli-Fox/LEGO-Catalog

//    companion object {

        // For Singleton instantiation
//        @Volatile
//        private var instance: AppDatabase? = null

//        fun getInstance(context: Context): AppDatabase {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
//        private fun buildDatabase(context: Context): AppDatabase {
//            return Room.databaseBuilder(context, AppDatabase::class.java, "legocatalog-db")
//                .addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                        WorkManager.getInstance(context).enqueue(request)
//                    }
//                })
//                .build()
//        }
//    }
//}