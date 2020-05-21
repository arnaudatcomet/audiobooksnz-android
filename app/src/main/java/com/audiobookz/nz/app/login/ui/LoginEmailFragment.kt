package com.audiobookz.nz.app.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import javax.inject.Inject


class LoginEmailFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel
    var Username: EditText? =null
    var Password: EditText? =null
    var LoginBtn: Button? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Username = view.findViewById(R.id.EditTextEmail) as EditText
        Password = view.findViewById(R.id.EditTextPassword) as EditText
        LoginBtn = view.findViewById(R.id.btnLogin) as Button
        viewModel = injectViewModel(viewModelFactory)
        LoginBtn!!.setOnClickListener { view ->
            if(Password!!.text.toString()=="" || Username!!.text.toString() == "")
            {
                Toast.makeText(getActivity(), "Username or password is empty", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.loginEmail(Username?.text.toString(),Password?.text.toString())
            }
        }
        subscribeUi()
    }
    private fun subscribeUi() {
        viewModel.logInResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    LoginBtn?.setText("Login")
                        val intent = Intent(activity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                }
                Result.Status.LOADING ->  LoginBtn?.setText("Loading")
                Result.Status.ERROR -> {
                    LoginBtn?.setText("Login")
                    Toast.makeText(getActivity(),"Username or Password is incorrect" ,Toast.LENGTH_SHORT).show();3
                }
            }
        })
    }
}