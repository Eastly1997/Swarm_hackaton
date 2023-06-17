package com.lakbay.pamayanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.appbar.AppBarLayout
import com.lakbay.pamayanan.adapters.VariationAdapter
import com.lakbay.pamayanan.databinding.ActivityProductDetailBinding
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.Product
import java.lang.Exception
import kotlin.math.abs

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var product: Product
    private var total: Double = 0.0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        initToolbar()
        
        try {
            product = intent.getSerializableExtra(CommonConstants.PRODUCT) as Product
            binding.product = this.product
            total = product.price
            binding.total = CommonUtils.convertToAmount(total)
        } catch (e : Exception) {
            finish()
        }

        CommonUtils.loadImage(this, product.img, binding.productImage)

        binding.variationList.adapter = VariationAdapter(this, product.variationFinal,
            object : VariationAdapter.BtnClickListener {
                override fun updateTotal(price: Double) {
                    total += price
                    binding.total = CommonUtils.convertToAmount(total)
                }
            })
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        binding.back.setOnClickListener {
            onBackPressed()
        }


        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                collapsed()
            } else {
                expanded()
            }
        })
    }

    private fun collapsed() {
        binding.collapsingToolbar.title = product.name
        binding.back.background = null
    }

    private fun expanded() {
        binding.collapsingToolbar.title = ""
        binding.back.background = AppCompatResources.getDrawable(this, R.drawable.round_background)
    }
}