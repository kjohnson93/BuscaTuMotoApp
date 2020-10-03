package com.buscatumoto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.buscatumoto.data.local.dao.FieldsDao
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.dao.SearchDao
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.SearchEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.utils.data.SeedDatabaseWorker
import com.buscatumoto.utils.data.Converters


/**
 * The Room database for this app
 */
@Database(entities = [SearchEntity::class, FieldsEntity::class, MotoEntity::class],
    version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchDao(): SearchDao
    abstract fun fieldsDao(): FieldsDao
    abstract fun motoDao(): MotoDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "buscatumotodb")
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}