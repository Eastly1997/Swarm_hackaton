package com.lakbay.pamayanan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lakbay.pamayanan.adapters.ViewPagerAdapter
import com.lakbay.pamayanan.databinding.ActivityMainBinding
import com.lakbay.pamayanan.fragments.HomeFragment
import com.lakbay.pamayanan.fragments.ProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue.increment
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.Donation
import com.lakbay.pamayanan.viewModels.Goal
import com.lakbay.pamayanan.viewModels.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchText: EditText
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>

    private val db = Firebase.firestore
    private val usersRef = db.collection(CommonConstants.FIREBASE_USER)
    private val goalRef = db.collection(CommonConstants.FIREBASE_GOAL)
    private var currentUser: User = User()
    private var currentDonation: Donation = Donation()
    var topList: ArrayList<User> = ArrayList<User>()
    var goalList: ArrayList<Goal> = ArrayList<Goal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        homeFragment = HomeFragment()
        viewPagerAdapter.addFragment(homeFragment, "HOME")
//        viewPagerAdapter.addFragment(MapFragment(), "MAP")
        viewPagerAdapter.addFragment(ProfileFragment(), "PROFILE")

        binding.pager.adapter = viewPagerAdapter
        binding.pager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.pager, true) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
//            val badge = tab.orCreateBadge
//            badge.number = 99
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.removeBadge()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        launchSomeActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val spokenText: String? =
                        data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                            .let { results ->
                                results!![0]
                            }
                    searchText.setText(spokenText)
                    //TODO : Search
                }
            }

        getUserData()
        getGoals()
        getTopDonation(5)
        displayLoading(false)
    }

    private fun getUserData() {
        usersRef.document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            currentUser = it.toObject<User>()!!
            SharedPrefUtils.saveData(this, User.FIELD_USER_NAME, currentUser.userName)
            SharedPrefUtils.saveData(this, User.FIELD_DONATED_AMOUNT, currentUser.donatedAmount.toFloat())
            SharedPrefUtils.saveData(this, User.FIELD_EARNING_AMOUNT, currentUser.earningAmount.toFloat())
            SharedPrefUtils.saveData(this, User.FIELD_UID, currentUser.uid)
            SharedPrefUtils.saveData(this, User.FIELD_IMG, currentUser.img)
            SharedPrefUtils.saveData(this, User.FIELD_MOBILE_NUMBER, currentUser.mobileNumber)
            homeFragment.individualAdGenerated = currentUser.earningAmount
            homeFragment.individualAdDonated = currentUser.donatedAmount
            homeFragment.binding.individualDonated = CommonUtils.convertToAmount(currentUser.donatedAmount)
            homeFragment.binding.individualEarned = CommonUtils.convertToAmount(currentUser.earningAmount)
        }
    }

    fun updateEarnedAmount(amount: Double) {
        Log.d("FIREBASE", "Update Amount: $amount")
        val finalAmount = amount * CommonConstants.AD_PERCENTAGE
        usersRef.document(currentUser.uid).update(User.FIELD_EARNING_AMOUNT, increment(finalAmount))
            .addOnSuccessListener {
                homeFragment.setlAdGenerated(finalAmount)
            }
    }

    private fun getGoals() {
        goalRef.get().addOnSuccessListener {
            var totalDonation = 0.00
            for(document in it.documents) {
                val goal = document.toObject<Goal>()
                if(goal != null) {
                    goalList.add(goal)
                    totalDonation += goal.donation
                }
            }
            homeFragment.loadGoalList(goalList)
            homeFragment.binding.totalEarned = CommonUtils.convertToAmount(totalDonation)
            SharedPrefUtils.saveData(this, Donation.FIELD_TOTAL, totalDonation.toFloat())
        }
    }

    private fun getTopDonation(limit: Int) {
        usersRef.orderBy(User.FIELD_DONATED_AMOUNT, Query.Direction.DESCENDING)
            .limit(limit.toLong()).get().addOnSuccessListener {
                for(document in it.documents) {
                    topList.add(document.toObject<User>()!!)
                    homeFragment.loadTopDonationList(topList)

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

    fun displayLoading(isDisplay: Boolean) {
        binding.commonLoading.visibility = if(isDisplay) View.VISIBLE else View.GONE
    }
}