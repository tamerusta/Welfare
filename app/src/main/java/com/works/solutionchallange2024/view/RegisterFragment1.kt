package com.works.solutionchallange2024.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentRegister1Binding
import com.works.solutionchallange2024.model.UserCredentials
import java.util.regex.Pattern

class RegisterFragment1 : Fragment() {

    private var userCredentials: UserCredentials? = null

    private var _binding: FragmentRegister1Binding? = null
    private val binding get() = _binding!!

    private lateinit var fullNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var loginTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegister1Binding.inflate(inflater, container, false)
        val view = binding.root

        fullNameEditText = view.findViewById(R.id.editTextFirstAndLastName)
        usernameEditText = view.findViewById(R.id.editTextUsername)
        emailEditText = view.findViewById(R.id.editTextEmail)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        loginTextView = view.findViewById(R.id.textViewLoginAction)

        loginTextView.setOnClickListener {
            val action = RegisterFragment1Directions.registerToLoginTransition()
            findNavController().navigate(action)
        }

        // Add TextWatchers to EditTexts
        fullNameEditText.addTextChangedListener(FullNameTextWatcher())
        usernameEditText.addTextChangedListener(UsernameTextWatcher())
        emailEditText.addTextChangedListener(EmailTextWatcher())
        editTextPhone.addTextChangedListener(PhoneNumberTextWatcher())

        binding.btnNextPage.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phoneNumber = editTextPhone.text.toString().trim()

            val nameParts = fullName.split(" ")
            val firstName = nameParts.getOrNull(0) ?: ""
            val lastName = nameParts.drop(1).joinToString(" ")

            when {
                fullName.isBlank() -> {
                    showToast("Name and surname cannot be left blank!")
                }

                username.isBlank() -> {
                    showToast("Username cannot be left blank!")
                }

                email.isBlank() -> {
                    showToast("Email cannot be left blank!")
                }

                phoneNumber.isBlank() -> {
                    showToast("Phone number cannot be left blank!")
                }

                !isValidName(firstName) || !isValidName(lastName) -> {
                    showToast("The first letter in the name and surname must be capitalized and consist only of letters.")
                }

                !isValidUsername(username) -> {
                    showToast("Username is invalid! It cannot consist of only numbers or only special characters.")
                }

                !isValidEmail(email) -> {
                    showToast("Invalid email format!")
                }

                !isValidPhoneNumber(phoneNumber) -> {
                    showToast("Invalid phone number!")
                }

                else -> {

                    showToast("You can continue registration.")

                        val transition=RegisterFragment1Directions.registerTransition(nameAndSurname =binding.editTextFirstAndLastName.text.toString(),
                            email=binding.editTextEmail.text.toString(), phone = binding.editTextPhone.text.toString(), userName = binding.editTextUsername.text.toString())
                        Navigation.findNavController(it).navigate(transition)

                }
            }
        }

        return view
    }

    private inner class FullNameTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString().trim()
            if (text.contains(" ")) {
                fullNameEditText.error = null
            } else {
                fullNameEditText.error = "Leave a space between the name and surname."
            }
        }
    }

    private inner class UsernameTextWatcher : SimpleTextWatcher() {
        override val editText: EditText
            get() = usernameEditText

        override fun validate(text: String): Boolean {
            return isValidUsername(text)
        }

        override fun getErrorMessage(): String {
            return "It cannot consist of only numbers or only special characters."
        }
    }

    private inner class EmailTextWatcher : SimpleTextWatcher() {
        override val editText: EditText
            get() = emailEditText

        override fun validate(text: String): Boolean {
            return isValidEmail(text)
        }

        override fun getErrorMessage(): String {
            return "Enter your e-mail in the format example@example.com."
        }
    }

    private inner class PhoneNumberTextWatcher : SimpleTextWatcher() {
        override val editText: EditText
            get() = editTextPhone

        override fun validate(text: String): Boolean {
            return isValidPhoneNumber(text)
        }

        override fun getErrorMessage(): String {
            return "Enter your phone number without writing 0 or country code."
        }
    }

    private abstract class SimpleTextWatcher : TextWatcher {
        abstract val editText: EditText

        abstract fun validate(text: String): Boolean

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let { editable ->
                val text = editable.toString()
                val isValid = validate(text)
                if (!isValid) {
                    editText.error = getErrorMessage()
                } else {
                    editText.error = null
                }
            }
        }

        open fun getErrorMessage(): String = "invalid entry"
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidName(name: String): Boolean {
        val onlyLettersRegex = "^[a-zA-ZğüşıöçĞÜŞİÖÇ]+$"
        val validNameRegex = "^[A-ZĞÜŞİÖÇa-zğüşıöçĞÜŞİÖÇ][a-zA-ZğüşıöçĞÜŞİÖÇ]*$"
        return if (Pattern.matches(onlyLettersRegex, name) && Pattern.matches(
                validNameRegex,
                name
            )
        ) {
            name[0].isUpperCase()
        } else {
            false
        }
    }

    private fun isValidUsername(username: String): Boolean {
        val usernameLengthMin = 3
        val usernameLengthMax = 32
        val letterCount = username.count { it.isLetter() }
        val nonLetterCount = username.length - letterCount
        val onlyDigitsRegex = "^[0-9]+$"
        val onlySpecialCharsRegex = "^[._-]+$"
        return username.length in usernameLengthMin..usernameLengthMax &&
                nonLetterCount < letterCount * 2 &&
                !(Pattern.matches(onlyDigitsRegex, username) || Pattern.matches(
                    onlySpecialCharsRegex,
                    username
                ))
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^[0-9]{10,15}$"
        return phoneNumber.matches(Regex(phoneNumberPattern))
    }
}