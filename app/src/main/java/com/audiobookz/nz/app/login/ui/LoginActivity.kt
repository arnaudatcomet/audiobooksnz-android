package com.audiobookz.nz.app.login.ui

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.register.ui.SignUpActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    //separate @inject for viewModelFactory initial
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel
    lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        setContentView(R.layout.activity_login)

        val btnLoginFacebook = findViewById<Button>(R.id.btnLoginFacebook)
        val btnLoginGoogle = findViewById<Button>(R.id.btnLoginGoogle)
        val btnLoginEmail = findViewById<Button>(R.id.btnLoginEmail)
        val btnSignUp = findViewById<Button>(R.id.btnSignFree)
        val btnDiscover = findViewById<Button>(R.id.btnDiscover)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("395719303570-d8bgers9haa3nu2ghqf7rp3p88mjt8tc.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //facebook
        callbackManager = CallbackManager.Factory.create()
        facebookLoginIn()

        btnLoginFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true)
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
            }
        }

        btnLoginGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(
                signInIntent, RC_SIGN_IN
            )
        }

        btnLoginEmail.setOnClickListener {
            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnDiscover.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, true)
            }
            startActivity(intent)
        }

        subscribeUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        //google
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleSignInResult(task)
        }
    }

    private fun facebookLoginIn() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                viewModel.facebookToken(loginResult?.accessToken?.token.toString())
            }
            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }
            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subscribeUi() {
        viewModel.loginGoogleResult.observe(this, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    //never go back if done
                    this.finish()
                }
                Result.Status.LOADING ->  {}
                Result.Status.ERROR -> {
                    Toast.makeText(this,Result.Status.ERROR.toString() ,Toast.LENGTH_SHORT).show();3
                }
            }
        })

        viewModel.loginFacebookResult.observe(this, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    //never go back if done
                    this.finish()
                }
                Result.Status.LOADING ->  {}
                Result.Status.ERROR -> {
                    Toast.makeText(this,Result.Status.ERROR.toString() ,Toast.LENGTH_SHORT).show();3
                }
            }
        })

    }

}
