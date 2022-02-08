package com.funtease.practice.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funtease.practice.databinding.ItemJobExploreBinding

class JobAdapterExplore(var jobList: ArrayList<String>) : RecyclerView.Adapter<JobAdapterExplore.ViewHolder>() {

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
        }
    }

}
