package com.works.solutionchallange2024.view

import ProductAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.adapter.LastProcessAdapter
import com.works.solutionchallange2024.databinding.FragmentHomePageBinding
import com.works.solutionchallange2024.service.LocalDatabase
import com.works.solutionchallange2024.service.TagsDao
import com.works.solutionchallange2024.viewmodel.HomePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var lastProcessAdapter: LastProcessAdapter
    private lateinit var homepageViewModel: HomePageViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var db: LocalDatabase
    private lateinit var dao: TagsDao
    private lateinit var binding: FragmentHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root

        homepageViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[HomePageViewModel::class.java]

        db = LocalDatabase.databaseAccess(requireContext())!!
        dao = db.getTags()

        homepageViewModel.getAllProductTags()
        homepageViewModel.getProductListByCity()
        homepageViewModel.getLastActionsHistory()

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(getContext(), 2)
        recyclerView = binding.productsRecyclerView

        homepageViewModel.list.observe(viewLifecycleOwner) {
            productAdapter = ProductAdapter(it)
            recyclerView.adapter = productAdapter
            recyclerView.layoutManager = layoutManager
            productAdapter.updateData(it)

        }

        val lastProcessRecyclerView: RecyclerView =
            view.findViewById(R.id.last_process_recycler_view)

        homepageViewModel.actionList.observe(viewLifecycleOwner) {
            lastProcessAdapter = LastProcessAdapter(it)
            lastProcessRecyclerView.adapter = lastProcessAdapter
            lastProcessRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showBottomNavigation()

        val args: HomePageFragmentArgs by navArgs()
        val category = args.category
        if (category != "default") {
            homepageViewModel.getProductListByCategory(category)
        }

        binding.homeFragmentMenuImageView.setOnClickListener {
            val gecis = HomePageFragmentDirections.actionHomePageFragmentToCategoryFragment()
            Navigation.findNavController(it).navigate(gecis)
        }

        homepageViewModel.tagList.observe(viewLifecycleOwner) { tagList ->
            val arrayList = ArrayList<String>()
            tagList.data.forEach { tag ->
                Log.e("TAG", tag.tag)
                arrayList.add(tag.tag)
            }

            //if elements in the list are deleted, run only once!!
            //homepageViewModel.insertTagList(arrayList, dao)
            //homepageViewModel.deleteAllTag(dao)
        }

        val listView = binding.listView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        loadTags()

        val searchText = binding.searchText

        searchText.setOnClickListener {
            binding.listView.visibility = View.VISIBLE
        }

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    loadTags()
                    val itemHeight = resources.getDimension(R.dimen._248pxh).toInt()
                    listView.layoutParams.height = itemHeight
                } else {
                    search(s.toString())
                    listView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedItem = adapter.getItem(position)
            Log.e("Clicked item", "$clickedItem at position $position")
            homepageViewModel.getProductListByTag(clickedItem!!)

            searchText.text = Editable.Factory.getInstance().newEditable(clickedItem)
            binding.listView.visibility = View.INVISIBLE
        }

        binding.layout.setOnClickListener {
            binding.listView.visibility = View.INVISIBLE
        }

        binding.productsRecyclerView.setOnClickListener {

        }
    }

    private fun loadTags() {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val list = dao.allTags()

            adapter.clear()
            for (tag in list) {
                adapter.add(tag.tag_name)
            }
        }
    }

    private fun search(keyword: String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val list = dao.searchTag("%$keyword%")

            adapter.clear()
            for (tag in list) {
                adapter.add(tag.tag_name)
            }
            adapter.notifyDataSetChanged()
        }
    }
}


