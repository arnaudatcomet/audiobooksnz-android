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

    fun savePositionPlay(position:Long, bookId:Int, chapter:Int){
        shrPref.edit().putLong(share_preferen_position_play+"$bookId$chapter", position).apply()
    }
    fun saveBookChapterSize(bookId:Int, size:Int){
        shrPref.edit().putInt(share_preferen_book+"size$bookId", size).apply()}

    fun saveBookDuration(bookId:Int, duration:Long){
        shrPref.edit().putLong(share_preferen_book+"duration$bookId", duration).apply()
    }
    fun saveBookCurrentChapter(bookId:Int, chapter:Int){
        shrPref.edit().putInt(share_preferen_current_chapter_of_the_book+"$bookId", chapter).apply()
    }
    fun saveBookReadComplete(bookId: Int, boolean: Boolean){
        shrPref.edit().putBoolean(share_preferen_book_complete+"$bookId", boolean).apply()
    }

    fun deleteToken() { shrPref.edit().remove(share_preferen_token_key).apply() }
    fun deleteCountTime() { shrPref.edit().remove(share_preferen_currentSleepTime).apply() }
    fun getToken(): String? = shrPref.getString(share_preferen_token_key, null)
    fun getSleepTime(): Long = shrPref.getLong(share_preferen_currentSleepTime, 0)
    fun getSavePositionPlay(bookId: Int, chapter: Int):Long = shrPref.getLong(share_preferen_position_play+"$bookId$chapter", 0)
    fun getBookChapterSize(bookId:Int):Int = shrPref.getInt(share_preferen_book+"size$bookId", 0)
    fun getBookDuration(bookId:Int):Long = shrPref.getLong(share_preferen_book+"duration$bookId", 0)
    fun getSaveBookCurrentChapter(bookId:Int):Int = shrPref.getInt(share_preferen_current_chapter_of_the_book+"$bookId",0)
    fun getsaveBookReadComplete(bookId:Int):Boolean = shrPref.getBoolean(share_preferen_book_complete+"$bookId",false)

}