package com.lakbay.pamayanan.fragments

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.firestore.ktx.toObject
import com.lakbay.pamayanan.CuisineModel
import com.lakbay.pamayanan.MainActivity
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.adapters.AdsInternalAdapter
import com.lakbay.pamayanan.adapters.CuisineAdapter
import com.lakbay.pamayanan.adapters.ListRestaurantVerticalAdapter
import com.lakbay.pamayanan.databinding.FragmentHomeFoodBinding
import com.lakbay.pamayanan.utils.CenterZoomLinearLayoutManager
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.FirebaseUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.Restaurant
import com.lakbay.pamayanan.viewModels.User


class HomeFoodFragment : Fragment() {

    lateinit var binding: FragmentHomeFoodBinding
    private var mRewardedAd: RewardedAd? = null
    private lateinit var mainActivity: MainActivity
    var individualAdGenerated: Double = 0.00
    var individualAdDonated: Double = 0.00

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentHomeFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.displayAds.setOnClickListener {
            displayAds()
        }

        individualAdGenerated = SharedPrefUtils
            .getFloatData(requireContext(), User.FIELD_LOYALTY_POINTS).toDouble()

        individualAdDonated = SharedPrefUtils
            .getFloatData(requireContext(), User.FIELD_LOYALTY_POINTS).toDouble()

        binding.individualEarned = CommonUtils.convertToAmount(individualAdGenerated)

        mainActivity = requireActivity() as MainActivity

        setUpRecyclerView()
        loadRewardAds()
    }

    private fun setUpRecyclerView() {
        setUpInternalAds()
        setUpCuisine()
        setUpVerticalRestaurant()
    }

    private fun setUpVerticalRestaurant() {
        val restaurantList = ArrayList<Restaurant>()
        FirebaseUtils.getRestaurantRef(mainActivity)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(FirebaseUtils.TAG, "getRestaurant()")

                    for (document in it.result) {
                        restaurantList.add(document.toObject<Restaurant>())
                    }
                    binding.listRestaurantVertical.adapter = ListRestaurantVerticalAdapter(restaurantList, mainActivity)
                } else {
                    Log.d(FirebaseUtils.TAG, "Error getting documents: ", it.exception)
                }
            }


    }

    private fun setUpCuisine() {
        with(binding.listCuisine) {
            val cuisineList = ArrayList<CuisineModel>()
            cuisineList.add(CuisineModel("Burger", R.drawable.ic_burger))
            cuisineList.add(CuisineModel("Cake", R.drawable.ic_cake))
            cuisineList.add(CuisineModel("Dessert", R.drawable.ic_dessert))
            cuisineList.add(CuisineModel("Donut", R.drawable.ic_donut))
            cuisineList.add(CuisineModel("Burger", R.drawable.ic_burger))
            cuisineList.add(CuisineModel("Cake", R.drawable.ic_cake))
            cuisineList.add(CuisineModel("Dessert", R.drawable.ic_dessert))
            cuisineList.add(CuisineModel("Donut", R.drawable.ic_donut))
            adapter = CuisineAdapter(cuisineList, mainActivity)
        }
    }
    private fun setUpInternalAds() {
        with(binding.listAdsInternal) {
            layoutManager = CenterZoomLinearLayoutManager(requireActivity().baseContext, 1f, 0.175f)
            val jobList = ArrayList<String>()
            jobList.add("REMINISCE PHOTOGRAPHY")
            jobList.add("TIMPLADO STUDIO CAFE")
            jobList.add("REMINISCE PHOTOGRAPHY")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            adapter = AdsInternalAdapter(jobList, width, requireActivity())
            val helper: SnapHelper = LinearSnapHelper()
            helper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position: Int = (layoutManager as CenterZoomLinearLayoutManager).findFirstVisibleItemPosition()
                        val lastItem = (adapter as AdsInternalAdapter).jobList.size - 2
                        if (position == 0) {
                            smoothScrollToPosition(1)
                        } else if(position == lastItem) {
                            smoothScrollToPosition(lastItem)
                        }
                    }
                }
            })
            smoothScrollToPosition(1)
        }
    }

    private fun loadRewardAds() {
        val testDeviceIds = listOf("4F337F0EA1E0F9DF9AA7B1BDAA84B2D2")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        requestRewardAds()

        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                mRewardedAd = null
                requestRewardAds()
            }
        }
    }

    private fun requestRewardAds() {
        Log.d("GOOGLE_ADS", "Reward Ads Requested")

        val adRequest = AdRequest.Builder().build()

        //Prod
//        val adUnit = "ca-app-pub-5106113422678211/8889551687"
        //Testing
        val adUnit = "ca-app-pub-3940256099942544/5224354917"

        RewardedAd.load(mainActivity, adUnit, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("GOOGLE_ADS", "Error: " + adError.message)
                binding.displayAds.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                binding.displayAds.isEnabled = false
                binding.displayAds.text = "Ads not yet ready"
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
                binding.displayAds.background.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY)
                binding.displayAds.isEnabled = true

            }
        })
    }

    private fun displayAds() {
        if (mRewardedAd != null) {
            mRewardedAd!!.show(mainActivity) { rewardItem -> // Handle the reward.
                Log.d("GOOGLE_ADS", "The user earned the reward.")
                Handler(Looper.getMainLooper()).postDelayed({
                    mainActivity.updateEarnedAmount(rewardItem.amount.toDouble())
                    Log.d("GOOGLE_ADS", "Amount: " + rewardItem.amount)
                    Log.d("GOOGLE_ADS", "Type: " + rewardItem.type)
                    requestRewardAds()
                }, 2000)
            }
        }
    }

    fun setAdGenerated(amount: Double) {
        individualAdGenerated += amount

        val valueAnimator = ValueAnimator.ofFloat((individualAdGenerated - amount).toFloat(),
            individualAdGenerated.toFloat())
        valueAnimator.duration = 1500
        valueAnimator.addUpdateListener { valueAnimator ->
            binding.individualEarned = CommonUtils.convertToAmount(valueAnimator.animatedValue.toString().toDouble())
        }
        valueAnimator.start()

    }
}