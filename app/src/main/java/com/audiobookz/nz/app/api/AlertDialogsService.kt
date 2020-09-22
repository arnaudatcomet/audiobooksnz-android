package com.audiobookz.nz.app.api

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialogsService(var context: Context) {
    fun simple(title:String,body:String){
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(body)
            .setPositiveButton("ACCEPT") { dialog, which ->
            }
            .show()
    }
}