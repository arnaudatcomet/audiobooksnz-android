package com.audiobookz.nz.app.api

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesService(var context: Context) {

    var share_preferen: String = "Share"
    var share_preferen_token_key: String = "Token"
    var share_preferen_currentSleepTime: String = "SleepTime"
    var share_preferen_position_play:String = "PositionBook"
    var share_preferen_book:String = "Book"
    var share_preferen_current_chapter_of_the_book:String = "TheBookCurrentChapter"
    var share_preferen_book_complete:String = "IsBookComplete"
    private val shrPref: SharedPreferences = context.getSharedPreferences(share_preferen, Context.MODE_PRIVATE)

    fun saveToken(accessToken: String) { shrPref.edit().putString(share_preferen_token_key, accessToken).apply() }
    fun saveSleepTime(countTime:Long){ shrPref.edit().putLong(share_preferen_currentSleepTime, countTime).apply() }

    fun savePositionPlay(position:Long, contentId:Int, chapter:Int){
        shrPref.edit().putLong(share_preferen_position_play+"$contentId$chapter", position).apply()
    }
    fun saveBookChapterSize(contentId:Int, size:Int){
        shrPref.edit().putInt(share_preferen_book+"size$contentId", size).apply()}

    fun saveBookDuration(contentId:Int, duration:Long){
        shrPref.edit().putLong(share_preferen_book+"duration$contentId", duration).apply()
    }
    fun saveBookCurrentChapter(contentId:Int, chapter:Int){
        shrPref.edit().putInt(share_preferen_current_chapter_of_the_book+"$contentId", chapter).apply()
    }
    fun saveBookReadComplete(contentId: Int, boolean: Boolean){
        shrPref.edit().putBoolean(share_preferen_book_complete+"$contentId", boolean).apply()
    }

    fun deleteToken() { shrPref.edit().remove(share_preferen_token_key).apply() }
    fun deleteCountTime() { shrPref.edit().remove(share_preferen_currentSleepTime).apply() }
    fun getToken(): String? = shrPref.getString(share_preferen_token_key, null)
    fun getSleepTime(): Long = shrPref.getLong(share_preferen_currentSleepTime, 0)
    fun getSavePositionPlay(contentId: Int, chapter: Int):Long = shrPref.getLong(share_preferen_position_play+"$contentId$chapter", 0)
    fun getBookChapterSize(contentId:Int):Int = shrPref.getInt(share_preferen_book+"size$contentId", 0)
    fun getBookDuration(contentId:Int):Long = shrPref.getLong(share_preferen_book+"duration$contentId", 0)
    fun getSaveBookCurrentChapter(contentId:Int):Int = shrPref.getInt(share_preferen_current_chapter_of_the_book+"$contentId",0)
    fun getsaveBookReadComplete(contentId:Int):Boolean = shrPref.getBoolean(share_preferen_book_complete+"$contentId",false)

}