package com.audiobookz.nz.app.login.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDataDao {
    @Query("SELECT * FROM userdata ")
    fun getUserData(): LiveData<UserData>

    @Query("SELECT access_token FROM userdata ")
    fun getAccessToken(): String

    @Query("DELETE FROM userdata ")
    fun logout()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(Userdata: UserData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckMailData(SuccessData: SuccessData)
}