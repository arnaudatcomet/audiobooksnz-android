package com.audiobookz.nz.app.bookdetail.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData


@Dao
interface BookRoomDao {
    @Query("SELECT COUNT(id) FROM bookroom ")
    fun getCount(): LiveData<Integer>
    @Insert
    fun insertCart(bookRoom: BookRoom)

    @Query("SELECT * FROM bookroom")
    fun loadBasket(): LiveData<List<BookRoom>>
}