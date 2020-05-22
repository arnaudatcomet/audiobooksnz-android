package com.audiobookz.nz.app.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.di.injectViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ForgotPasswordActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        var resetBtn = findViewById<Button>(R.id.btnResetPassword)
        var emailEdit = findViewById<EditText>(R.id.EditTextEmail)
        viewModel = injectViewModel(viewModelFactory)

        resetBtn.setOnClickListener { view ->
            if (emailEdit.text.isNotEmpty()) {

            } else {
                Toast.makeText(this, "email is empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
