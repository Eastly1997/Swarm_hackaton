package com.lakbay.pamayanan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.databinding.ItemJobBinding
import com.lakbay.pamayanan.utils.CommonUtils


class JobAdapter(var jobList: ArrayList<String>, var width: Int, private val context: Context) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(jobList[position], position)

    override fun getItemCount() = jobList.size

    inner class ViewHolder(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: String, position: Int) {

            val params: RecyclerView.LayoutParams = binding.jobCard.layoutParams as RecyclerView.LayoutParams
            val commonUtils = CommonUtils.instance

            if(position == 0 || position == jobList.size - 1) {
                binding.jobCard.visibility = View.INVISIBLE
                params.width = commonUtils.convertDptoPx(40)
                params.height = commonUtils.convertDptoPx(80)
                binding.jobCard.layoutParams = params
            } else {
                params.width = width - commonUtils.convertDptoPx(40)
                binding.jobCard.layoutParams = params
                val url = "https://www.philippinetourismusa.com/wp-content/uploads/2019/05/palawan-slider-min.jpg"
                commonUtils.loadImage(context, url, binding.jobImg)
            }

            binding.jobTitleString = job;

            binding.showAds = View.OnClickListener {

            }

        }
    }

}

