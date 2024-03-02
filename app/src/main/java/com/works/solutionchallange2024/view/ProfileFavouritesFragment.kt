package com.works.solutionchallange2024.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.adapter.FavAdapter
import com.works.solutionchallange2024.common.Categories
import com.works.solutionchallange2024.config.AppDatabase
import com.works.solutionchallange2024.databinding.FragmentProfileFavouritesBinding
import com.works.solutionchallange2024.model.FavData
import com.works.solutionchallange2024.model.room.FavouriteAdvert
import com.works.solutionchallange2024.service.FavDao
import com.works.solutionchallange2024.viewmodel.ProfileFavouritesViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFavouritesFragment : Fragment() , FavAdapter.OnItemClickListener {
    private lateinit var _binding: FragmentProfileFavouritesBinding
    private lateinit var viewModel : ProfileFavouritesViewModel

    private val binding get() = _binding
    val items = mutableListOf<FavouriteAdvert>()
    private var recyclerView: RecyclerView? = null
    private var productAdapter: FavAdapter? = null
    private lateinit var db : AppDatabase
    private lateinit var dao : FavDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProfileFavouritesViewModel::class.java]

        _binding = FragmentProfileFavouritesBinding.inflate(inflater, container, false)

        val item1 = FavouriteAdvert("https://imvm.letgo.com/v1/files/365f7f0360ee4-OLXAUTOTR/image;s=780x0;q=60","Alper",4.2,"250",Categories.hobby,"15 /",true)
        val item2 = FavouriteAdvert("https://imvm.letgo.com/v1/files/7ef0b794b0524-OLXAUTOTR/image;s=1080x1080","TmrUsta",3.5,"200",Categories.baby,"5 /",true)
        val item3 = FavouriteAdvert("https://imvm.letgo.com/v1/files/m3icl8au98go1-OLXAUTOTR/image;s=780x0;q=60","Shyonei",2.7,"250",Categories.clothing,"10 /",true)
        val item4 = FavouriteAdvert("https://imvm.letgo.com/v1/files/3b37f23638804-OLXAUTOTR/image;s=780x0;q=60","Beko12",1.9,"250",Categories.electronis,"20 /",true)
        items.add(item1)
        items.add(item2)
        items.add(item3)
        items.add(item4)

        db = AppDatabase.databaseAccess(requireContext())!!
        dao = db.FavDao()

        recyclerView = binding.recyclerView
        adapterFun()

        //loadFavItems()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showBottomNavigation()
    }
    private fun adapterFun(){
        productAdapter = FavAdapter(requireActivity(), items)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 1)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = productAdapter
    }

    override fun onItemClick(position: Int) {
        Log.e("onItemClicked", "Clicked item at position: $position")
        productAdapter!!.notifyItemChanged(position)
    }
}


