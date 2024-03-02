package com.works.solutionchallange2024.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.model.retrofit.Advert
import com.works.solutionchallange2024.view.ActivitiesFragmentDirections

class ActivitiesAdapter(
    private val context: Context,
    private var items: List<Advert>,
    private val isSelling: Boolean,
    private val isPurchase: Boolean

) : RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder>() {

    inner class ActivitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView1: CardView = itemView.findViewById(R.id.cardView)
        val imageView1: ImageView = itemView.findViewById(R.id.productPhoto)
        val priceTextView1: TextView = itemView.findViewById(R.id.textViewPoint)
        val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val imageview2: ImageView = itemView.findViewById(R.id.imageView4)
        val tagText: TextView = itemView.findViewById(R.id.textViewTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ActivitiesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sell_card_view, parent, false)
        return ActivitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivitiesAdapter.ActivitiesViewHolder, position: Int) {
        Glide.with(context).load(items[position].images[0]).into(holder.imageView1)
        holder.priceTextView1.text = items[position].point.toString()
        holder.categoryTextView.text = items[position].category
        holder.tagText.text = items[position].tag
        holder.cardView1.setOnClickListener {
            val item = items[position]
            if (isSelling) {
                val action =
                    ActivitiesFragmentDirections.actionActionsFragmentToSellPageFragment(item._id)
                holder.itemView.findNavController().navigate(action)
            } else if (isPurchase) {
                val action =
                    ActivitiesFragmentDirections.actionActionsFragmentToProductBuyDetailFragment2(
                        item._id
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        when (items[position].drawCompleted) {
            false -> {
                holder.statusTextView.text = "Waiting"
                holder.imageview2.setImageResource(R.drawable.baseline_alarm_24)
                holder.statusTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.duskYellow
                    )
                )
            }

            else -> {
                holder.statusTextView.text = "Completed"
                holder.statusTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newProductList: List<Advert>) {
        items = newProductList
        notifyDataSetChanged()
    }
}