package com.audiobookz.nz.app.mylibrary.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SessionDataDao {
    @Query("SELECT * FROM session ")
    fun getSessionData(): LiveData<SessionData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionData(sessionData: SessionData)
}