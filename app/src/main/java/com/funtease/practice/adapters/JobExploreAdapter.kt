package com.funtease.practice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funtease.practice.databinding.ItemJobExploreBinding
import com.funtease.practice.utils.CommonUtils

class JobExploreAdapter(var jobList: ArrayList<String>, val context : Context) : RecyclerView.Adapter<JobExploreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobExploreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(jobList[position])

    override fun getItemCount() = jobList.size

    inner class ViewHolder(private val binding: ItemJobExploreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: String) {
            binding.jobTitleString = job;
            val url = "https://www.philippinetourismusa.com/wp-content/uploads/2019/05/palawan-slider-min.jpg"
            CommonUtils().loadCurvedImage(context, url, binding.jobImg, 25)
        }
    }

}
