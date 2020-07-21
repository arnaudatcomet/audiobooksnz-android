package com.audiobookz.nz.app.mylibrary.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChapterDataDao {

    @Transaction
    @Query("SELECT * FROM chapterData where id = :chapterId ")
    fun getLocalBookData(chapterId:Int): LiveData<ChapterData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserChapterData(chapterData: ChapterData)

    @Query("DELETE FROM chapterData WHERE contentId = :contentId")
    fun deleteByContentId(contentId:Int)

}