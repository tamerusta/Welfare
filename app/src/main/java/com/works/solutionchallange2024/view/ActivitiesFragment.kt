package com.works.solutionchallange2024.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.adapter.ActivitiesAdapter
import com.works.solutionchallange2024.databinding.FragmentActivitiesBinding
import com.works.solutionchallange2024.model.ItemsData
import com.works.solutionchallange2024.model.retrofit.Advert
import com.works.solutionchallange2024.viewmodel.ActivitiesViewModel
import com.works.solutionchallange2024.viewmodel.HomePageViewModel

class ActivitiesFragment : Fragment() {

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var activitiesAdapter: ActivitiesAdapter
    private var items = mutableListOf<ItemsData>()
    private var recyclerView: RecyclerView? = null

    private lateinit var activiesViewModel : ActivitiesViewModel
    var isPurchase = false
    var isSelling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)

        activiesViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[ActivitiesViewModel::class.java]

        val purchasesBtn = binding.purchasesBtn
        val sellingBtn = binding.sellingsBtn
        recyclerView = binding.recyclerView

        activiesViewModel.fetchAllMySellings()

        purchasesBtn.setOnClickListener {
            activiesViewModel.fetchAllMyPurchases()
            purchasesBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.dark_green2))
            sellingBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.light_green2))
        }
        sellingBtn.setOnClickListener {
            activiesViewModel.fetchAllMySellings()
            sellingBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.dark_green2))
            purchasesBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.light_green2))
        }

        activiesViewModel.list.observe(viewLifecycleOwner){
            adapterFun(it)
        }

        binding.addBtn.setOnClickListener {
            val action = ActivitiesFragmentDirections.actionActionsFragmentToSell2Fragment()
            findNavController().navigate(action)
        }

        activiesViewModel.isSelling.observe(viewLifecycleOwner){
            isSelling = !it
        }

        activiesViewModel.isPurchase.observe(viewLifecycleOwner){
            isPurchase = !it
        }

        return binding.root
    }

    private fun adapterFun(items: List<Advert>) {

        activitiesAdapter = ActivitiesAdapter(requireActivity(), items, isPurchase,isSelling)
        activitiesAdapter.updateData(items)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = activitiesAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





