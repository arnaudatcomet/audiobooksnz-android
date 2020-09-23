package com.audiobookz.nz.app.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject


class ForgotPasswordFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var resetBtn = view.findViewById<Button>(R.id.btnResetPassword)
        var emailEdit = view.findViewById<EditText>(R.id.EditTextEmail)
        viewModel = injectViewModel(viewModelFactory)

        resetBtn.setOnClickListener { view ->
            if (emailEdit.text.isNotEmpty()) {
                viewModel.resetPass(emailEdit.text.toString())
                subscribeUi()
            } else {
                Toast.makeText(activity, "email is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeUi() {
        viewModel.emailResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data == null) {
                        Toast.makeText(activity, "e-mail not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, result.data.success, Toast.LENGTH_SHORT).show();3
                    }

                }
                Result.Status.LOADING -> {
                    Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();3
                }
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })
    }
}
