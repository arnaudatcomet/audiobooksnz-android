package com.audiobookz.nz.app

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_facebooklogin.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    var firebaseAuth : FirebaseAuth? = null
    var callbackManager : CallbackManager? = null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebooklogin)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        btn_facebook.setReadPermissions("email")
        btn_facebook.setOnClickListener {
            signIn()
        }


        printKeyHash()
    }

    private fun signIn() {
        btn_facebook.registerCallback(callbackManager,object:FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnFailureListener {e->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener { result ->
                val email = result.user.email
                val name = result.user.displayName
                Toast.makeText(this, "Your Email : " + email, Toast.LENGTH_LONG).show()
                Log.e("Email", email)
                Log.e("Username", name)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo("com.audiobookz.nz.app", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e:PackageManager.NameNotFoundException)
        {

        }
        catch (e:NoSuchAlgorithmException)
        {

        }
    }
}
