package com.lakbay.pamayanan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.databinding.ItemAdsInternalBinding
import com.lakbay.pamayanan.utils.CommonUtils


class AdsInternalAdapter(var jobList: ArrayList<String>, var width: Int, private val context: Context) : RecyclerView.Adapter<AdsInternalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdsInternalBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(jobList[position], position)

    override fun getItemCount() = jobList.size

    inner class ViewHolder(private val binding: ItemAdsInternalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: String, position: Int) {

            val params: RecyclerView.LayoutParams = binding.jobCard.layoutParams as RecyclerView.LayoutParams

            if(position == 0 || position == jobList.size - 1) {
                binding.jobCard.visibility = View.INVISIBLE
                params.width = CommonUtils.convertDptoPx(40)
                params.height = CommonUtils.convertDptoPx(80)
                binding.jobCard.layoutParams = params
            } else {
                params.width = width - CommonUtils.convertDptoPx(40)
                binding.jobCard.layoutParams = params
                val url = "https://www.philippinetourismusa.com/wp-content/uploads/2019/05/palawan-slider-min.jpg"
                if(position != 1) {
                    CommonUtils.loadImage(context, url, binding.jobImg)
                } else {
                    CommonUtils.loadImage(context,  AppCompatResources.getDrawable(context, R.drawable.timplado)!!, binding.jobImg)
                    binding.location.text = "San Nicolas, Bulacan"
                }
            }

            binding.jobTitleString = job;

            binding.showAds = View.OnClickListener {

            }

        }
    }

}

