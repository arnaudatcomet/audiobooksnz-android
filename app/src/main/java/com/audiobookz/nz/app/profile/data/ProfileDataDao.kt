package com.audiobookz.nz.app.profile.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audiobookz.nz.app.login.data.UserData

@Dao
interface ProfileDataDao {
    @Query("SELECT * FROM userdata")
    fun getProfileData(): LiveData<UserData>

    @Query("SELECT access_token FROM userdata ")
    fun getAccessToken(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileData(Userdata: UserData)
}