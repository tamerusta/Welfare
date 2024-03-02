package com.works.solutionchallange2024.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.works.solutionchallange2024.databinding.MenuCategoryBinding
import com.works.solutionchallange2024.model.Category
import com.works.solutionchallange2024.view.CategoryFragmentDirections

class CategoryAdapter(var mContext: Context, var categoryListesi: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryDesignHolder>() {

    inner class CategoryDesignHolder(var design: MenuCategoryBinding) :
        RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDesignHolder {
        val binding = MenuCategoryBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CategoryDesignHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryDesignHolder, position: Int) {
        val category = categoryListesi[position]
        val t = holder.design
        t.categoryNameTextView.text = category.categoryName

        val requestOptions = RequestOptions().centerCrop()

        Glide.with(mContext)
            .load(category.categoryImageUrl)
            .apply(requestOptions)
            .into(t.categoryImageView)

        t.categoryCardView.setOnClickListener {
            val gecis =
                CategoryFragmentDirections.actionCategoryFragmentToHomePageFragment(category.categoryName)
            Navigation.findNavController(it).navigate(gecis)
        }
    }

    override fun getItemCount(): Int {
        return categoryListesi.size
    }
}