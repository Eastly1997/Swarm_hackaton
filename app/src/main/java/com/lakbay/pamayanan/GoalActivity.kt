package com.lakbay.pamayanan

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

import com.google.android.material.appbar.AppBarLayout
import com.lakbay.pamayanan.databinding.ActivityGoalBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.Goal

class GoalActivity :  AppCompatActivity() {

    private lateinit var binding: ActivityGoalBinding
    private lateinit var goal: Goal
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goal = intent.getSerializableExtra("GOAL") as Goal
        position = intent.getIntExtra("POSITION", 0)

        init()

    }

    private fun init() {
        var isShow = true
        var scrollRange = -1
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                binding.collapsingToolbar.title = goal.title
                isShow = true
            } else if (isShow){
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        })

        with(binding) {
            subTitle.text = goal.subTitle
            totalDonation.text = CommonUtils.convertToAmount(goal.donation)

            var img: Drawable? = null
            when (position) {
                0 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal1)
                1 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal2)
                2 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal3)
                3 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal4)
                4 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal5)
                5 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal6)
                6 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal7)
                7 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal8)
                8 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal9)
                9 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal10)
                10 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal11)
                11 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal12)
                12 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal13)
                13 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal14)
                14 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal15)
                15 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal16)
                16 -> img = AppCompatResources.getDrawable(this@GoalActivity, R.drawable.goal17)
            }

            CommonUtils.loadImage(this@GoalActivity, img!!, this.img)

            val hexColor = Color.parseColor(goal.color)
            collapsingToolbar.setBackgroundColor(hexColor)
            coordinatorLayout.setBackgroundColor(hexColor)
            layoutDonation.setBackgroundColor(hexColor)
            toolbar.setBackgroundColor(hexColor)

        }
    }
}