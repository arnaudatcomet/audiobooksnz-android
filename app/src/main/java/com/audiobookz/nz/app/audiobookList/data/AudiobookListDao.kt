package com.audiobookz.nz.app.audiobookList.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * The Data Access Object for the LegoTheme class.
 */
@Dao
interface AudiobookListDao {
    @Query("SELECT * FROM audiobookList ")
    fun getAudiobookList(): LiveData<List<AudiobookList>>
//    fun getCategoryDetail(): LiveData<List<CategoryDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudiobookList(AudiobookList: List<AudiobookList>)

    @Query("DELETE FROM audiobookList")
    fun nukeAudiobookList()
}