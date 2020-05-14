package com.audiobookz.nz.app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.audiobookz.nz.app.R
import com.google.android.material.textfield.TextInputEditText

class LoginEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)



        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edittxtAddress = findViewById<TextInputEditText>(R.id.editEmailAddress)
        val edittxtPass = findViewById<TextInputEditText>(R.id.editPassword)

        btnLogin.setOnClickListener { view ->

           val msg: String = edittxtAddress.text.toString() + " " + edittxtPass.text.toString()


            if (edittxtAddress.text.toString().trim().isNotEmpty() && edittxtPass.text.toString().trim().isNotEmpty()){
                Toast.makeText(this, msg , Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "E-Mail or Password is Empty" , Toast.LENGTH_SHORT).show()
            }

        }

        val txtforgotPass = findViewById<TextView>(R.id.txtForgotPass)
        txtforgotPass.setOnClickListener { view ->
            Toast.makeText(this, "forgot Password", Toast.LENGTH_SHORT).show()
        }
    }
}
