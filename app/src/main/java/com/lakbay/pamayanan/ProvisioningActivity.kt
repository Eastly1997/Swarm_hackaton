package com.lakbay.pamayanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import com.lakbay.pamayanan.adapters.InfoGraphicsAdapter
import com.lakbay.pamayanan.databinding.ActivityProvisioningBinding
import com.lakbay.pamayanan.utils.DotIndicatorDecorator


class ProvisioningActivity : BaseActivity() {
    lateinit var binding: ActivityProvisioningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvisioningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        with(binding) {
            infoGraphicsList.adapter = InfoGraphicsAdapter(this@ProvisioningActivity)
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(infoGraphicsList)
            infoGraphicsList.addItemDecoration(DotIndicatorDecorator())

            signupClick = View.OnClickListener {
                val intent = Intent(this@ProvisioningActivity, RegisterUserActivity::class.java)
                startActivity(intent)
            }

        }
    }

}