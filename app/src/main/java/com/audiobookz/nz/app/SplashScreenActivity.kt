package com.audiobookz.nz.app

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.login.ui.LoginActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        AsyncTask.execute {
            // Get Data
            val accessToken: String = AppDatabase.getInstance(
                applicationContext
            ).userDataDao().getAccessToken()
            if(accessToken!=null){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
