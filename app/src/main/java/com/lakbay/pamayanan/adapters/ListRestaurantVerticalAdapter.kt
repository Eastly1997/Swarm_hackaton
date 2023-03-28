package com.lakbay.pamayanan.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.databinding.ItemListRestaurantVerticalBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.FirebaseUtils
import com.lakbay.pamayanan.viewModels.Restaurant

class ListRestaurantVerticalAdapter(var restaurantList: ArrayList<Restaurant>, val context : Context) : RecyclerView.Adapter<ListRestaurantVerticalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListRestaurantVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(restaurantList[position])

    override fun getItemCount() = restaurantList.size

    inner class ViewHolder(private val binding: ItemListRestaurantVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            Log.e("Restaurant", restaurant.img)
            binding.nameString = restaurant.name
            binding.ratingString = restaurant.rating.toString()
            CommonUtils.loadCurvedImage(context, FirebaseUtils.getRestaurantLogoRef(context, restaurant.uid) , binding.restaurantImg, 15)

            if(restaurant.isOpen())
                binding.openHours.text = "Open until " + restaurant.getClosing()
            else
                binding.openHours.text = "Opening in " + restaurant.getOpening()

        }
    }

}
