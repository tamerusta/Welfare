package com.works.solutionchallange2024.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentProfileChangePasswordBinding

class ProfileChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentProfileChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileChangePasswordBinding.inflate(inflater, container, false)

        binding.profileInformationBackImageView.setOnClickListener {
            val action = ProfileChangePasswordFragmentDirections.actionProfileChangePasswordFragmentToProfileInformationFragment()
            findNavController().navigate(action)
        }

        binding.profileChangePasswordButton.setOnClickListener {
            val currentPassword = binding.profileCurrentPasswordEditText.text.toString()
            val newPassword = binding.profileNewPasswordEditText.text.toString()
            val confirmPassword = binding.profileConfirmPasswordEditText.text.toString()
            val passwordStrength = calculatePasswordStrength(newPassword)
            val rightPassword = "Erdem123."

            when {
                currentPassword.isBlank() -> {
                    Toast.makeText(
                        requireContext(),
                        "Current password cannot be left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                newPassword.isBlank() -> {
                    Toast.makeText(
                        requireContext(),
                        "New password cannot be left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                confirmPassword.isBlank() -> {
                    Toast.makeText(
                        requireContext(),
                        "Confirm password cannot be left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                rightPassword != currentPassword -> {
                    Toast.makeText(requireContext(), "The current password is incorrect.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                confirmPassword != newPassword -> {
                    Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                !isValidPassword(newPassword) -> {
                    Toast.makeText(
                        requireContext(),
                        "Password is invalid! It must be at least 6 characters long and must not contain spaces.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                passwordStrength < 4 -> {
                    Toast.makeText(
                        requireContext(),
                        "Your password is too weak. Please choose a stronger password.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
        binding.imageView5.setOnClickListener {
         findNavController().navigate(R.id.action_profileChangePasswordFragment_to_profileFragment)
        }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showBottomNavigation()
    }

    private fun isValidPassword(currentPassword: String): Boolean {
        val passwordLengthMin = 6
        val passwordLengthMax = 32

        return currentPassword.length in passwordLengthMin..passwordLengthMax && !currentPassword.contains(" ")
    }

    private fun calculatePasswordStrength(currentPassword: String): Int {
        var strength = 0
        val hasLowercase = currentPassword.any { it.isLowerCase() }
        val hasUppercase = currentPassword.any { it.isUpperCase() }
        val hasDigit = currentPassword.any { it.isDigit() }
        val hasSpecialChar = currentPassword.any { !it.isLetterOrDigit() }

        if (hasLowercase) strength++
        if (hasUppercase) strength++
        if (hasDigit) strength++
        if (hasSpecialChar) strength++

        return strength
    }






}