package com.works.solutionchallange2024.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.works.solutionchallange2024.R

class ViewPagerAdapterSellDetail(private val imageUrlList:ArrayList<String>, private val viewPager2: ViewPager2)
    :RecyclerView.Adapter<ViewPagerAdapterSellDetail.ImageViewHolder>(){


    class ImageViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.sliderItemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item,parent,false)
        return ImageViewHolder(view)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)

    }
    override fun getItemCount(): Int {
        return imageUrlList.size
    }




}