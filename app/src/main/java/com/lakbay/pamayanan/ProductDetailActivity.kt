package com.lakbay.pamayanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lakbay.pamayanan.databinding.ActivityProductDetailBinding
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.viewModels.Product
import java.lang.Exception

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var product: Product
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        try {
            product = intent.getSerializableExtra(CommonConstants.PRODUCT) as Product
        } catch (e : Exception) {
            finish()
        }
    }
}