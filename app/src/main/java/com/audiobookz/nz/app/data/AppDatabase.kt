package com.audiobookz.nz.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.bookdetail.data.BookRoomDao
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.login.data.UserDataDao
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardDataDao
import com.audiobookz.nz.app.mylibrary.data.*
import com.audiobookz.nz.app.worker.SeedDatabaseWorker

/**
 * The Room database for this app
 */
@Database(entities = [UserData::class,BookRoom::class,SuccessData::class,SessionData::class,LocalBookData::class,ChapterData::class,CardData::class],
        version = 34, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDataDao(): UserDataDao
    abstract fun bookRoomDao(): BookRoomDao
    abstract fun sessionDataDao():SessionDataDao
    abstract fun localBookDataDao():LocalBookDataDao
    abstract fun chapterDataDao():ChapterDataDao
    abstract fun cardDataDao(): CardDataDao

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
