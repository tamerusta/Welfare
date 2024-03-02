package com.works.solutionchallange2024.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.adapter.CategoryAdapter
import com.works.solutionchallange2024.common.Categories
//import com.works.solutionchallange2024.common.Categories
import com.works.solutionchallange2024.databinding.FragmentCategoryBinding
import com.works.solutionchallange2024.model.Category

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imageviewBackButton.setOnClickListener{
            findNavController().navigate(R.id.action_categoryFragment_to_homePageFragment)
        }

        binding.categoryFragmentCategoryRecyclerView.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)

        val categoryList= ArrayList<Category>()
        val c1=Category(0,Categories.electronis,"https://n11scdn.akamaized.net/a1/320_480/14/42/72/37/IMG-927437339729790549.jpg")
        val c2=Category(1,Categories.clothing,"https://www.burford.co.uk/cdn/shop/articles/DSC_1322-2.width-1280_aHJJfxnf9cTCTpBX.jpg?v=1697462936")
        val c3=Category(2,Categories.baby,"https://static.ticimax.cloud/cdn-cgi/image/width=-,quality=85/57821/uploads/urunresimleri/buyuk/alas-baby-bebek-bakim-cantasi-pembe-09bb56.jpg")
        val c4=Category(3,Categories.hobby,"https://img.freepik.com/free-vector/illustration-passion_53876-26976.jpg?size=626&ext=jpg")
        val c5=Category(4,Categories.furnishing,"https://i0.wp.com/www.re-thinkingthefuture.com/wp-content/uploads/2021/04/A3848-How-to-enhance-Soft-Furnishing-Aesthetics-in-your-house.jpg?w=999")
        val c6=Category(5,Categories.musical,"https://media.istockphoto.com/id/894058154/tr/foto%C4%9Fraf/m%C3%BCzik-aletleri.jpg?s=612x612&w=0&k=20&c=t5fomPyxybJfW3H6gfflLakzY2Az2xVcZDpCQlEfxGo=")

        categoryList.add(c1)
        categoryList.add(c2)
        categoryList.add(c3)
        categoryList.add(c4)
        categoryList.add(c5)
        categoryList.add(c6)

        val categoryAdapter=CategoryAdapter(requireContext(),categoryList)
        binding.categoryFragmentCategoryRecyclerView.adapter=categoryAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).hideBottomNavigation()
    }

}
