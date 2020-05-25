package com.audiobookz.nz.app

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.login.ui.LoginActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharePref = getSharedPreferences("Token", Context.MODE_PRIVATE)
        // Get Data
        val accessToken = sharePref?.getString("Token", "")

        if (accessToken != null && accessToken != "") {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
