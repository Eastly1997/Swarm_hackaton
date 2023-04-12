package com.lakbay.pamayanan.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.ProductDetailActivity
import com.lakbay.pamayanan.databinding.HeaderProductBinding
import com.lakbay.pamayanan.databinding.ItemProductBinding
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.Product

class ProductVerticalAdapter(private val context: Context, private var productList: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }


    inner class ItemViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            CommonUtils.loadCurvedImage(context, product.img, binding.productImage, 20)
            binding.viewProduct = View.OnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(CommonConstants.PRODUCT, product)
                context.startActivity(intent)
            }
        }

    }

    inner class HeaderViewHolder(private val binding: HeaderProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.name = category
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = HeaderProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false)
                return HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(productList[position].name)
            }
            is ItemViewHolder -> {
                holder.bind(productList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemViewType(position: Int): Int {
        // Determine the view type based on the position
        return if (productList[position].isCategoryHeader) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }
}