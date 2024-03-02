package com.works.solutionchallange2024.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.MainActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentProfileBinding
import com.works.solutionchallange2024.manager.AppPref

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater, container, false)

        binding.constraintLayoutUpdate.setOnClickListener{
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileInformationFragment()
            findNavController().navigate(action)
        }

        binding.constraintLayoutFavorites.setOnClickListener{
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileFavouritesFragment()
            findNavController().navigate(action)
        }

        binding.constraintLayoutLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return binding.root
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure want to log out?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val appPref = AppPref(requireContext())
            appPref.clearData()
            appPref.clearToken()
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showBottomNavigation()

        val ratingBar = view.findViewById<RatingBar>(R.id.profileRatingBar)
        val ratingTextView = view.findViewById<TextView>(R.id.profileRatingTextView)

        // Set the rating value (e.g. 4.7)
        val ratingValue = 4.7.toFloat()
        ratingBar.rating = ratingValue

        //Write rating value to TextView
        ratingTextView.text = String.format("%.1f", ratingValue)
    }
}