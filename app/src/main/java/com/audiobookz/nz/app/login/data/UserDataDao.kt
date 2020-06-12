package com.audiobookz.nz.app.login.data

import androidx.lifecycle.LiveData
import androidx.room.*


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

    @Query("UPDATE userdata set credit_count = :credit ")
    suspend fun updateUsersCredit(credit: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckMailData(SuccessData: SuccessData)
}