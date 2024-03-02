package com.works.solutionchallange2024.view

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso.Listener
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.MainActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentLoginBinding
import com.works.solutionchallange2024.manager.AppPref
import com.works.solutionchallange2024.model.retrofit.UserLogin
import com.works.solutionchallange2024.viewmodel.LoginViewModel
import com.works.solutionchallange2024.viewmodel.RegisterViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val emailTxt = binding.editTextUsernameLogin
        val passwordTxt = binding.editTextPasswordLogin
        val register = binding.textViewRegisterLink
        val rememberMeBox = binding.checkBoxRememberMe
        val loginBtn = binding.btnLogin

        val appPref = AppPref(requireContext())

        // Navigation
        val bundle: LoginFragmentArgs by navArgs()
        val mail = bundle.email

        if (mail.isNotEmpty()) {
            binding.editTextUsernameLogin.setText(mail)
        }

            binding.imagePadlock.setOnClickListener {
            binding.editTextPasswordLogin.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        }

        var rememberMe = rememberMeBox.isChecked

        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[LoginViewModel::class.java]

        rememberMeBox.setOnCheckedChangeListener { buttonView, isChecked ->
            rememberMe = isChecked
        }

        loginViewModel.errorMessageLiveData.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        loginViewModel.setLoginListener(object : LoginViewModel.LoginListener {
            override fun onLoginSuccess(token: String) {
                if (rememberMe) {
                    appPref.saveToken(token)
                    appPref.userData(email, password, rememberMe)
                    appPref.setIsChecked(true)
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    appPref.saveToken(token)
                    appPref.clearData()
                    appPref.setIsChecked(false)
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

            override fun onLoginFailure(error: String) {
                appPref.clearToken()
            }
        })

        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment1)
        }

        loginBtn.setOnClickListener {
            email = emailTxt.text.toString()
            password = passwordTxt.text.toString()

            if (!emailTxt.text.isNotEmpty()) {
                Toast.makeText(context, "Username/email cannot be left blank", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (!emailTxt.text.isNotEmpty()) {
                Toast.makeText(context, "Password cannot be left blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                loginViewModel.Login(email, password)
            }
        }
        return binding.root
    }
}