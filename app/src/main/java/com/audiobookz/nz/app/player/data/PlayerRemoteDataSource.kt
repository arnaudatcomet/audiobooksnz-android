package com.audiobookz.nz.app.player.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PlayerRemoteDataSource @Inject constructor(private val service: AudiobookService) :
    BaseDataSource() {

    fun postSyncPlayBackPosition(cloudBookID: Int, chapter: Int, position: Long, part: Int) =
         service.postSyncPlayBackPosition(cloudBookID, chapter, position, part)

    suspend fun getSyncPlayBackPosition(cloudBookID: Int) =
        getResult { service.getSyncPlayBackPosition(cloudBookID) }

    suspend fun postBookmarks(cloudBookId: Int, chapter: RequestBody, position: RequestBody, subtitle: RequestBody, part: RequestBody, title: RequestBody)=
        getResult { service.postBookmars(cloudBookId,chapter,position,subtitle,part,title) }

    suspend fun getBookmarks(cloudBookId: Int, page: Int, pageSize: Int) =
        getResult { service.getBookmarks(cloudBookId, page, pageSize) }

    fun  deleteBookmark(bookmarkId:Int) = service.deleteBookmark(bookmarkId)

    suspend fun updateBookmark(bookmarkId:Int, title:String) = getResult { service.updateBookmarks(bookmarkId,title) }

    suspend fun postBookReview(bookId: Int, comment: String, statification: Float, story: Float, narration: Float)
            = getResult { service.postBookReview(bookId,comment,statification,story,narration) }

    suspend fun fetchSession() = getResult { service.getSession() }
}