package com.audiobookz.nz.app.register.ui

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.SplashScreenActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentSignUpBinding
import com.audiobookz.nz.app.login.ui.LoginEmailActivity
import javax.inject.Inject

class SignUpFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SignUpViewModel
    private var isSignUpPro: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentSignUpBinding.inflate(inflater, container, false)
        isSignUpPro = activity?.intent!!.getBooleanExtra(EXTRA_MESSAGE, false)
        binding.isSignUpPro = isSignUpPro
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = injectViewModel(viewModelFactory)

        val edittxtFistname = view.findViewById<EditText>(R.id.editFirstName)
        val edittxtLastname = view.findViewById<EditText>(R.id.editLastName)
        val edittxtEmail = view.findViewById<EditText>(R.id.editEmail)
        val edittxtPassword = view.findViewById<EditText>(R.id.editPasswordSignUp)
        val edittxtPasswordConfirm = view.findViewById<EditText>(R.id.editPasswordConfirm)
        val btnSignup = view.findViewById<Button>(R.id.btnSignUp)
        val chkbox = view.findViewById<CheckBox>(R.id.checkboxTerm)

        btnSignup.setOnClickListener { view ->
            if (!chkbox.isChecked) {
                Toast.makeText(activity, "please tick checkbox", Toast.LENGTH_SHORT).show()
            } else if (edittxtFistname.text.isEmpty()) {
                Toast.makeText(activity, "firstname is blank", Toast.LENGTH_SHORT).show()
            } else if (edittxtLastname.text.isEmpty()) {
                Toast.makeText(activity, "lastname is blank", Toast.LENGTH_SHORT).show()
            } else if (edittxtEmail.text.isEmpty()) {
                Toast.makeText(activity, "email is blank", Toast.LENGTH_SHORT).show()
            } else if (edittxtPassword.text.isEmpty()) {
                Toast.makeText(activity, "password is blank", Toast.LENGTH_SHORT).show()
            } else if (edittxtPasswordConfirm.text.isEmpty()) {
                Toast.makeText(activity, "confirm password is blank", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.emailSignUp(
                    edittxtEmail.text.toString(),
                    edittxtLastname.text.toString(),
                    edittxtPassword.text.toString(),
                    "1",
                    edittxtPasswordConfirm.text.toString(),
                    edittxtFistname.text.toString()
                )
            }
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data == null) {
                        Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();
                    } else {
                        if (isSignUpPro) {
                            result.data.access_token?.let { viewModel.signUpPro(it) }
                        } else {
                            Toast.makeText(activity, "Sign Up Success", Toast.LENGTH_SHORT).show();
                            val intent = Intent(
                                activity,
                                SplashScreenActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }

                    }
                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data != null) {
                        val navController = Navigation.findNavController(view!!)
                        navController.navigate(
                            SignUpFragmentDirections.actionSignUpEmailFragmentToPayPalWebViewFragment(
                                result.data.approval_link,
                                "SignUpPro"
                            )
                        )
                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

    }

}
