package com.funtease.practice.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.funtease.practice.adapters.JobAdapter
import com.funtease.practice.databinding.FragmentHomeBinding
import com.funtease.practice.utils.CenterZoomLinearLayoutManager
import com.funtease.practice.utils.CommonUtils
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var mRewardedAd: RewardedAd? = null
    private var isDestroyed = false
    lateinit var jobAdapter: JobAdapter
    private var individualAdGenerated: Double = 0.00

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.displayAds.setOnClickListener {
           displayAds()
        }
        binding.totalEarned = CommonUtils.instance.convertToAmount(individualAdGenerated)
        setUpRecyclerView()
        loadRewardAds()
    }


    private fun setUpRecyclerView() {
        with(binding.jobList) {
            layoutManager = CenterZoomLinearLayoutManager(requireActivity().baseContext, 1f, 0.175f)
            val jobList = ArrayList<String>()
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            adapter = JobAdapter(jobList, width, requireActivity())
            val helper: SnapHelper = LinearSnapHelper()
            helper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position: Int = (layoutManager as CenterZoomLinearLayoutManager).findFirstVisibleItemPosition()
                        val lastItem = (adapter as JobAdapter).jobList.size - 2
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

    override fun onDestroy() {
        isDestroyed = true
        super.onDestroy()
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

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
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
        val adUnit = "ca-app-pub-5106113422678211/8889551687"
        //Testing
//        val adUnit = "ca-app-pub-3940256099942544/5224354917"
        RewardedAd.load(activity, adUnit, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("GOOGLE_ADS", "Error: " + adError.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd

            }
        })
    }

    private fun displayAds() {
        if (mRewardedAd != null) {
            mRewardedAd!!.show(activity) { rewardItem -> // Handle the reward.
                Log.d("GOOGLE_ADS", "The user earned the reward.")
                individualAdGenerated += rewardItem.amount
                binding.totalEarned = CommonUtils.instance.convertToAmount(individualAdGenerated)
                Log.d("GOOGLE_ADS", "Amount: " + rewardItem.amount)
                Log.d("GOOGLE_ADS", "Type: " + rewardItem.type)
                requestRewardAds()
            }
        }
    }

}

