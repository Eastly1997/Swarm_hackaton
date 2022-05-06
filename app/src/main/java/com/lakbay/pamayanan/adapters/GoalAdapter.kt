package com.lakbay.pamayanan.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.databinding.ItemGoalBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.Goal


class GoalAdapter(private val goalList: ArrayList<Goal>, private val context: Context) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(goalList[position], position)

    override fun getItemCount() = goalList.size

    inner class ViewHolder(private val binding: ItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goal, position: Int) {
            binding.goal = goal
            binding.donationProgress.progress += goal.donation.toInt()
            val hexColor = Color.parseColor(goal.color)
            var buttonDrawable: Drawable = binding.goalDonate.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, hexColor);
            binding.goalDonate.background = buttonDrawable

            var progressDrawable: Drawable = binding.donationProgress.progressDrawable
            progressDrawable = DrawableCompat.wrap(progressDrawable);
            DrawableCompat.setTint(progressDrawable, hexColor);
            binding.donationProgress.progressDrawable = progressDrawable

            when (position) {
                0 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal1)
                1 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal2)
                2 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal3)
                3 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal4)
                4 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal5)
                5 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal6)
                6 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal7)
                7 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal8)
                8 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal9)
                9 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal10)
                10 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal11)
                11 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal12)
                12 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal13)
                13 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal14)
                14 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal15)
                15 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal16)
                16 -> goal.img = AppCompatResources.getDrawable(context, R.drawable.goal17)
            }

            CommonUtils.loadImage(context, goal.img!!, binding.goalImg)
        }
    }
}