package com.lakbay.pamayanan.adapters

import android.R.attr.button
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.databinding.ItemGoalBinding
import com.lakbay.pamayanan.viewModels.Goal


class GoalAdapter(private val goalList: ArrayList<Goal>) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(goalList[position])

    override fun getItemCount() = goalList.size

    inner class ViewHolder(private val binding: ItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goal) {
            binding.goal = goal
            binding.donationProgress.progress = goal.donation.toInt()
            binding.goalName.setBackgroundColor(Color.parseColor("#${goal.hexColor}"))
            var buttonDrawable: Drawable = binding.goalDonate.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#${goal.hexColor}"));
            binding.goalDonate.background = buttonDrawable

            var progressDrawable: Drawable = binding.donationProgress.progressDrawable
            progressDrawable = DrawableCompat.wrap(progressDrawable);
            DrawableCompat.setTint(progressDrawable, Color.parseColor("#${goal.hexColor}"));
            binding.donationProgress.progressDrawable = progressDrawable
        }
    }
}