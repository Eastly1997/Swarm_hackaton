package com.lakbay.pamayanan.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.databinding.ItemInfoGraphicsBinding
import com.lakbay.pamayanan.utils.CommonUtils

class InfoGraphicsAdapter   (val context: Context) :  RecyclerView.Adapter<InfoGraphicsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoGraphicsAdapter.ViewHolder {
        val binding = ItemInfoGraphicsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoGraphicsAdapter.ViewHolder, position: Int) {
        holder.bind( position)
    }

    override fun getItemCount() = 3

    inner class ViewHolder(private val binding: ItemInfoGraphicsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            var title = ""
            var subTitle = ""
            var drawable: Drawable? = null
            when(position) {
                0 -> {
                    title = context.getString(R.string.welcome_title_1)
                    subTitle = context.getString(R.string.welcome_subtitle_1)
                    drawable = AppCompatResources.getDrawable(context, R.drawable.earth_play)
                }
                1 -> {
                    title = context.getString(R.string.welcome_title_2)
                    subTitle = context.getString(R.string.welcome_subtitle_2)
                    drawable = AppCompatResources.getDrawable(context, R.drawable.donate_ads)
                }
                2 -> {
                    title = context.getString(R.string.welcome_title_3)
                    subTitle = context.getString(R.string.welcome_subtitle_3)
                    drawable = AppCompatResources.getDrawable(context, R.drawable.watch_ads)
                }
            }
            with(binding) {
                headerTitle.text = title
                headerSub.text = subTitle
                CommonUtils.loadImage(context, drawable!!, img)
            }
        }
    }
}