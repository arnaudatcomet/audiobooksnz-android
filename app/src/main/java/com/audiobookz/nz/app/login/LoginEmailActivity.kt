package com.audiobookz.nz.app.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.google.android.material.textfield.TextInputEditText

class LoginEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)



        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edittxtEmailAddress = findViewById<TextInputEditText>(R.id.editEmailAddress)
        val edittxtPass = findViewById<TextInputEditText>(R.id.editPassword)

        btnLogin.setOnClickListener { view ->

           val msg: String = edittxtEmailAddress.text.toString() + " " + edittxtPass.text.toString()


            if (edittxtEmailAddress.text.toString().trim().isNotEmpty() && edittxtPass.text.toString().trim().isNotEmpty()){
                Toast.makeText(this, msg , Toast.LENGTH_SHORT).show()
            } else{
                // bypass
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)            }

        }

        val txtforgotPass = findViewById<TextView>(R.id.txtForgotPass)
        txtforgotPass.setOnClickListener { view ->
            Toast.makeText(this, "forgot Password", Toast.LENGTH_SHORT).show()
        }
    }
}
