package com.audiobookz.nz.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.audiobookz.nz.app.audiobookList.data.AudiobookList
import com.audiobookz.nz.app.audiobookList.data.AudiobookListDao
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.categories.data.CategoryDao
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.login.data.UserDataDao
import com.audiobookz.nz.app.worker.SeedDatabaseWorker

/**
 * The Room database for this app
 */
@Database(entities = [Category::class,AudiobookList::class,UserData::class],
        version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun audiobookListDao(): AudiobookListDao
    abstract fun userDataDao(): UserDataDao


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "audiobooksnz-db")
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
