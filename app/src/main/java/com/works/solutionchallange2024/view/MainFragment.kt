package com.works.solutionchallange2024.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentMainBinding
import com.works.solutionchallange2024.manager.AppPref


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)

        val appPref = AppPref(requireContext())

        if(appPref.getIsChecked()){
            //The path to the home page will be written
           // findNavController().navigate(R.id.???)
        }

        binding.btnGetStarted.setOnClickListener {
            //goToRegisterActivity() path to sign up button
            findNavController().navigate(R.id.registerTransition)
        }

        return binding.root
    }
}