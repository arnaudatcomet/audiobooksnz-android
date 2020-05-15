package com.audiobookz.nz.app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.audiobookz.nz.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val edittxtFistname = findViewById<EditText>(R.id.editFirstName)
        val edittxtLastname = findViewById<EditText>(R.id.editLastName)
        val edittxtEmail = findViewById<EditText>(R.id.editEmail)
        val edittxtPassword = findViewById<EditText>(R.id.editPasswordSignUp)
        val edittxtPasswordConfirm = findViewById<EditText>(R.id.editPasswordConfirm)
        val btnSignup = findViewById<Button>(R.id.btnSignUp)
        val chkbox = findViewById<CheckBox>(R.id.checkboxTerm)

        btnSignup.setOnClickListener { view ->

            val msg: String =
                edittxtFistname.text.toString() + " " + edittxtLastname.text.toString()
            val msg2: String =
                edittxtEmail.text.toString() + " " + edittxtPassword.text.toString() + " " + edittxtPasswordConfirm.text.toString()
            val msg3 = "$msg $msg2"

            if (chkbox.isChecked && msg3.trim().isNotEmpty()) {
                Toast.makeText(this, msg3, Toast.LENGTH_SHORT).show()

            } else {
//                Toast.makeText(
////                    this,
////                    "Some text is Empty and please tick checkbox",
////                    Toast.LENGTH_SHORT
////                ).show()

                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.AlertTitle))
                    .setMessage(resources.getString(R.string.AlertDescription))
                    .setPositiveButton(resources.getString(R.string.AlertOk)) { dialog, which ->
                        // Respond to positive button press
                    }
                    .show()
            }

        }

    }
}
