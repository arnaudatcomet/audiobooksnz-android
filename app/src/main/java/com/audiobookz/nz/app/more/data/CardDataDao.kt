package com.audiobookz.nz.app.more.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDataDao {
    @Transaction
    @Query("SELECT * FROM carddata")
    fun getCardData(): LiveData<List<CardData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCardData(CardData: CardData)

    @Query("DELETE FROM carddata WHERE card_id = :card_id")
    fun deleteById(card_id:String)
}