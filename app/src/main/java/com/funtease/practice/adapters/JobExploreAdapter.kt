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
        holder.bind(jobList[position], position)

    override fun getItemCount() = jobList.size

    inner class ViewHolder(private val binding: ItemJobExploreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: String, position: Int) {
            binding.jobTitleString = job;
            var url = "https://play-lh.googleusercontent.com/1Ye9cdoHkBuGegkuoyJ1q5HVMeDgZUbkRJcU_5P2W20-J90Oa9wrJ_euTBEu84dfnA"
            when(position) {
                0-> url = "https://play-lh.googleusercontent.com/1Ye9cdoHkBuGegkuoyJ1q5HVMeDgZUbkRJcU_5P2W20-J90Oa9wrJ_euTBEu84dfnA"
                1-> url = "https://angelescity.ph/wp-content/uploads/cache/images/jollibee-logo/jollibee-logo-3567621404.jpg"
                2-> url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiYP0YmAMbNsH0yTxQW9Uhwflndrs7p_COQpk5gvXVSIbdKicBW2dBJvCOR2NUZVsrHas&usqp=CAU"
            }
            CommonUtils.instance.loadCurvedImage(context, url, binding.jobImg, 15)
        }
    }

}
