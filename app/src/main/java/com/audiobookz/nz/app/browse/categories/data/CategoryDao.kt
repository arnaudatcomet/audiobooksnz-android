package com.audiobookz.nz.app.browse.categories.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * The Data Access Object for the LegoTheme class.
 */
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getCategory(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(plants: List<Category>)
}
