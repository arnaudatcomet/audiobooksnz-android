package com.audiobookz.nz.app.login.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.browse.di.Injectable
import com.audiobookz.nz.app.browse.di.injectViewModel
import com.audiobookz.nz.app.data.Result
import javax.inject.Inject


class LoginEmailFragment : Fragment(), Injectable{
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

        Username!!.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.Username = s.toString();
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        Password!!.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.Password = s.toString();
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        Log.d("TAG", Username!!.getText().toString())
        LoginBtn!!.setOnClickListener { view ->
            if(Password!!.text.toString()=="" || Username!!.text.toString() == "")
            {
                Toast.makeText(getActivity(), "Username or password is empty", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.loginEmail;
                subscribeUi()
            }
        }
    }
    private fun subscribeUi() {
        viewModel.loginEmail.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
//                    binding.progressBarDetail.hide()
//                    result.data?.let { adapter.submitList(it) }
                    if(result.data==null){
                        Toast.makeText(getActivity(),"Username or password is incorrect" ,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"good" ,Toast.LENGTH_SHORT).show();
                        val intent = Intent(activity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }

                }
                Result.Status.LOADING ->    Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    Toast.makeText(getActivity(),result.message ,Toast.LENGTH_SHORT).show();
                }
            }
        })
    }
}