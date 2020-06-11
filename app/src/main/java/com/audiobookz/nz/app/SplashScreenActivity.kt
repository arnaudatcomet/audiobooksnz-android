package com.audiobookz.nz.app
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.login.ui.LoginActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharePref = SharedPreferencesService(this)

        val accessToken = sharePref.getToken()

        if (accessToken != null && accessToken != "") {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
