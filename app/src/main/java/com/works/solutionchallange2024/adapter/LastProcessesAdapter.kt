package com.works.solutionchallange2024.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.model.retrofit.Advert

class LastProcessAdapter(private val productList: List<Advert>) : RecyclerView.Adapter<LastProcessAdapter.ProductViewHolder>() {
    private val maxItemCount = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.last_process_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return minOf(productList.size, maxItemCount)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (position < maxItemCount) {
            val product = productList.getOrNull(position)
            product?.let {
                holder.bind(product)
            }
        }
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.name_last_product)
        private val productCategory: TextView = itemView.findViewById(R.id.category_last_product)
        private val productImage: ImageView = itemView.findViewById(R.id.img_last_products)
        private val productStatus : TextView = itemView.findViewById(R.id.info_last_product)

        fun bind(product: Advert) {
            // Null check ekleyin
            if (product != null) {
                productName.text = product.tag
                productCategory.text = product.category
                Picasso.get().load(product.images[0]).into(productImage)
                when (product.status) {
                    "active" -> {
                        productStatus.text = "Pending"
                        productStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.duskYellow))
                    }
                    "completed" -> {
                        productStatus.text = "Accepted"
                        productStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                    }
                }
            }
        }
    }

}