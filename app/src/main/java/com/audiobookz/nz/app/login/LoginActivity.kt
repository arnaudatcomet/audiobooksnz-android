package com.audiobookz.nz.app.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.audiobookz.nz.app.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnloginFacebook = findViewById<Button>(R.id.btnLoginFacebook)
        val btnloginGoogle = findViewById<Button>(R.id.btnLoginGoogle)
        val btnloginEmail = findViewById<Button>(R.id.btnLoginEmail)
        val btnSignUp = findViewById<Button>(R.id.btnSignFree)
        val btnDiscover = findViewById<Button>(R.id.btnDiscover)

        btnloginFacebook.setOnClickListener { view ->
            Toast.makeText(this, "loginfacebook", Toast.LENGTH_SHORT).show()
        }

        btnloginGoogle.setOnClickListener { view ->
            Toast.makeText(this, "loginGoogle", Toast.LENGTH_SHORT).show()
        }

        btnloginEmail.setOnClickListener { view ->
            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener { view ->
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnDiscover.setOnClickListener { view ->
            Toast.makeText(this, "Discover", Toast.LENGTH_SHORT).show()
        }
    }
}
