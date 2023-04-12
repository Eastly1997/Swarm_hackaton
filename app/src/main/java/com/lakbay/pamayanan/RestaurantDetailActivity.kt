package com.lakbay.pamayanan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.firestore.ktx.toObject
import com.lakbay.pamayanan.adapters.CategoryListHorizontalAdapter
import com.lakbay.pamayanan.adapters.ProductVerticalAdapter
import com.lakbay.pamayanan.databinding.ActivityRestaurantDetailBinding
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.FirebaseUtils
import com.lakbay.pamayanan.viewModels.Product
import com.lakbay.pamayanan.viewModels.ProductVariation
import com.lakbay.pamayanan.viewModels.Restaurant
import com.lakbay.pamayanan.viewModels.Variation
import kotlin.math.abs

class RestaurantDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var restaurant: Restaurant
    private var variation = mutableMapOf<String, Variation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        restaurant = intent.getSerializableExtra(CommonConstants.RESTAURANT) as Restaurant

        initToolbar()
        initRestaurantInfo()
        getVariation()
    }

    private fun getProduct() {
        FirebaseUtils.getRestaurantProductRef(this).whereEqualTo(Product.RESTAURANT_UID, restaurant.uid)
            .orderBy(Product.CATEGORY)
            .get()
            .addOnCompleteListener {
                val productList: ArrayList<Product> = ArrayList()
                if (it.isSuccessful) {
                    Log.d(FirebaseUtils.TAG, "getRestaurant()")

                    var lastCategory = ""
                    for (document in it.result) {
                        val product = document.toObject<Product>()
                        if(lastCategory != product.category) {
                            lastCategory = product.category
                            val category = Product()
                            category.category = lastCategory
                            category.name = lastCategory
                            category.isCategoryHeader = true
                            productList.add(category)
                        }
                        for(key in product.variation) {
                            variation[key]?.let { vary -> product.variationFinal.add(vary) }
                        }
                        productList.add(document.toObject())
                    }

                } else {
                    Log.d(FirebaseUtils.TAG, "Error getting documents: ", it.exception)
                }

                binding.productList.adapter = ProductVerticalAdapter(this, productList)

            }
    }

    private fun getVariation() {
        FirebaseUtils.getProductVariationRef(this).document(restaurant.uid)
            .get()
            .addOnSuccessListener {
                val restaurantVariation: ProductVariation= it.toObject<ProductVariation>()!!
                for (variety in restaurantVariation.variation_final) {
                    variation[variety.id] = variety
                }

                getProduct()
            }
            .addOnFailureListener {
                getProduct()
            }
    }


    private fun initRestaurantInfo() {
        binding.restaurant = restaurant
        CommonUtils.loadImage(this, restaurant.img, binding.restaurantImg)
        val categories = restaurant.category_product
        binding.categoryList.adapter = CategoryListHorizontalAdapter(categories)
        binding.categoryList.smoothScrollToPosition(0)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        binding.back.setOnClickListener {
            onBackPressed()
        }


        binding.appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                collapsed()
            } else {
                expanded()
            }
        })
    }

    private fun collapsed() {
        binding.collapsingToolbar.title = restaurant.name
        binding.back.background = null
    }

    private fun expanded() {
        binding.collapsingToolbar.title = ""
        binding.back.background = AppCompatResources.getDrawable(this, R.drawable.round_background)
    }

}