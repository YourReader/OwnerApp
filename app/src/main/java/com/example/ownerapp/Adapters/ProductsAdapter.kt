package com.example.ownerapp.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.ownerapp.data.Product
import com.example.ownerapp.databinding.ProductlistitemBinding

class ProductsAdapter(val context: Context) :
    ListAdapter<Product, ProductsAdapter.viewHolder>(DiffCallBack()) {
    private lateinit var mListener: onItemClickedListener


    interface onItemClickedListener {
        fun onEditButtonClicked(product: Product)
    }

    fun setOnEditClickListener(listener: onItemClickedListener) {
        mListener = listener
    }

    inner class viewHolder(private val binding: ProductlistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val text = "₹ ${product.price}"
            Log.d("TAG", "bind: BINDING THIS -$product")
            binding.apply {
                if (adapterPosition == 0)
                    topStrip.visibility = View.VISIBLE
                productNameCard.text = product.name
                productPrice.text = text
                Glide.with(context)
                    .load(product.productImage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            loadProgressLayout.visibility = View.GONE
                            Log.d(
                                "TAG",
                                "onLoadFailed: Failed to load image ${e!!.message}  \n\n ${e.cause} "
                            )
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            relativeBar.visibility = View.GONE
                            productInfoLay.visibility = View.VISIBLE
                            productImage.visibility = View.VISIBLE
                            loadProgressLayout.visibility = View.GONE
                            return false
                        }

                    })
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fitCenter()
                    .into(productImage)

                editButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        mListener.onEditButtonClicked(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            ProductlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

    }
}