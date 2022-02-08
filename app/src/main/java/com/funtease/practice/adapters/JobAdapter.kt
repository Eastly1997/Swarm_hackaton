package com.funtease.practice.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funtease.practice.databinding.ItemJobBinding


class JobAdapter(var jobList: ArrayList<String>, var width: Int) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

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
            if(position == 0 || position == jobList.size - 1) {
                binding.jobCard.visibility = View.GONE
            }

            val params: RecyclerView.LayoutParams = binding.jobCard.layoutParams as RecyclerView.LayoutParams
            params.width = width - (100 * Resources.getSystem().displayMetrics.density).toInt()
            binding.jobCard.layoutParams = params
            binding.jobTitleString = job;
        }
    }

}

