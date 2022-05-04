package com.lakbay.pamayanan

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.lakbay.pamayanan.databinding.ActivityProvisioningBinding

class ProvisioningActivity : AppCompatActivity() {
    lateinit var binding: ActivityProvisioningBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvisioningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.phone.requestFocus()
    }

    private fun init() {
        with(binding) {
            phone.doOnTextChanged { text, start, before, count ->
                binding.login.isClickable = text?.length == 10
                binding.login.isFocusable = text?.length == 10
                binding.login.setTextColor(if(text?.length == 10) resources.getColor(R.color.white) else
                    resources.getColor(R.color.white_50))
            }
            verifyPhoneClickListener = View.OnClickListener {
                val intent = Intent(this@ProvisioningActivity, PhoneAuthenticationActivity ::class.java)
                intent.putExtra("mobile_number", phone.text.toString())
                startActivity(intent)
            }
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        if(newOverride.fontScale > 1.3) {
            newOverride.fontScale = 1.3f
            applyOverrideConfiguration(newOverride)
        }
        super.attachBaseContext(newBase)
    }
}