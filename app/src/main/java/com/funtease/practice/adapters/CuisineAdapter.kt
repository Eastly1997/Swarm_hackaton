package com.funtease.practice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.funtease.practice.CuisineModel
import com.funtease.practice.R
import com.funtease.practice.databinding.ItemCuisineBinding
import com.funtease.practice.utils.CommonUtils


class CuisineAdapter(var cuisineList: ArrayList<CuisineModel>, val context: Context) : RecyclerView.Adapter<CuisineAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCuisineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cuisineList[position], position)
    }

    override fun getItemCount(): Int = cuisineList.count()

    inner class ViewHolder(private val binding: ItemCuisineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cuisine: CuisineModel, position: Int) {
            binding.cuisineImg.setImageDrawable(AppCompatResources.getDrawable(context, cuisine.img))
            if(position%2 == 0) {
                binding.cuisineImg.setBackgroundColor(context.getColor(R.color.cuisine_color1))
            } else {
                binding.cuisineImg.setBackgroundColor(context.getColor(R.color.cuisine_color2))
            }
        }
    }
}