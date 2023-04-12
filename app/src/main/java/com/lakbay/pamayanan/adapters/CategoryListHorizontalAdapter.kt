package com.lakbay.pamayanan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.databinding.ItemCategoryHorizontalBinding

class CategoryListHorizontalAdapter(private val categoryList: ArrayList<String>) : RecyclerView.Adapter<CategoryListHorizontalAdapter.ViewHolder>() {

    private var currentItem = -1

    inner class ViewHolder(private val binding: ItemCategoryHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, position: Int) {
            binding.name = name

            if (position == currentItem) {
                binding.underline.visibility = View.VISIBLE
            } else {
                binding.underline.visibility = View.INVISIBLE
            }

            binding.onClick = View.OnClickListener {
                val previousItem = currentItem
                currentItem = position

                notifyItemChanged(previousItem)
                notifyItemChanged(currentItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], position)
    }
}