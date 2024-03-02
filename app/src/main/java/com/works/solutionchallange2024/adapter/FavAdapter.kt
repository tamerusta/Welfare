package com.works.solutionchallange2024.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.model.room.FavouriteAdvert

class FavAdapter(
    private val context: Context,
    private var items: List<FavouriteAdvert>,

    ) :
    RecyclerView.Adapter<FavAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        return MyViewHolder(itemView)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.ProductPhoto)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val userName: TextView = itemView.findViewById(R.id.usernameTxt)
        val category: TextView = itemView.findViewById(R.id.product_categoryTXT)
        val score: TextView = itemView.findViewById(R.id.profileFavouritesListItemScore)
        val participationNum: TextView = itemView.findViewById(R.id.participationNumTxt)
        val favBtn: ImageView = itemView.findViewById(R.id.favoriteImage)

        init {
            if (category == null || score == null || participationNum == null || favBtn == null) {
                throw NullPointerException("One or more views are null in MyViewHolder")
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
        holder.score.text = currentItem.point.toString()
        holder.userName.text = currentItem.username.toString()
        holder.ratingBar.rating = currentItem.userRating.toFloat()
        holder.category.text = currentItem.category
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.titleImage)
        holder.participationNum.text = currentItem.participants
        if (currentItem.isFavourite) {
            holder.favBtn.setImageResource(R.drawable.filledheart)
        }
        holder.favBtn.setOnClickListener {
            var favourite = !currentItem.isFavourite
            if (favourite) {
                holder.favBtn.setImageResource(R.drawable.filledheart)

            } else {
                holder.favBtn.setImageResource(R.drawable.hollowheart)

            }
        }

    }
}