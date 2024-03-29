package com.lakbay.pamayanan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue.increment
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.adapters.ViewPagerMainAdapter
import com.lakbay.pamayanan.databinding.ActivityMainBinding
import com.lakbay.pamayanan.fragments.HomeFoodFragment
import com.lakbay.pamayanan.fragments.ProfileFragment
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.FirebaseUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.User


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerMainAdapter: ViewPagerMainAdapter
    private lateinit var homeFragment: HomeFoodFragment
    private lateinit var searchText: EditText
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>

    private var currentUser: User = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerMainAdapter = ViewPagerMainAdapter(supportFragmentManager, lifecycle)
        homeFragment = HomeFoodFragment()
        viewPagerMainAdapter.addFragment(homeFragment, "HOME")
        viewPagerMainAdapter.addFragment(ProfileFragment(), "PROFILE")

        binding.pager.adapter = viewPagerMainAdapter
        binding.pager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.pager, true) { tab, position ->
            tab.text = viewPagerMainAdapter.getTitle(position)
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

        if(Firebase.auth.currentUser != null) {
            getUserData()
        }
        displayLoading(false)
    }

    private fun getUserData() {
        FirebaseUtils.getUserRef(this)
            .document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            currentUser = it.toObject<User>()!!
            SharedPrefUtils.saveData(this, User.FIELD_USER_NAME, currentUser.userName)
            SharedPrefUtils.saveData(this, User.FIELD_LOYALTY_POINTS, currentUser.loyaltyPoints.toFloat())
            SharedPrefUtils.saveData(this, User.FIELD_UID, currentUser.uid)
            SharedPrefUtils.saveData(this, User.FIELD_IMG, currentUser.img)
            SharedPrefUtils.saveData(this, User.FIELD_MOBILE_NUMBER, currentUser.mobileNumber)
            homeFragment.individualAdGenerated = currentUser.loyaltyPoints
            homeFragment.individualAdDonated = currentUser.loyaltyPoints
            homeFragment.binding.individualEarned = CommonUtils.convertToAmount(currentUser.loyaltyPoints)
        }
    }

    fun updateEarnedAmount(amount: Double) {
        Log.d("FIREBASE", "Update Amount: $amount")
        val finalAmount = amount * CommonConstants.AD_PERCENTAGE
        FirebaseUtils.getUserRef(this)
            .document(currentUser.uid).update(User.FIELD_LOYALTY_POINTS, increment(finalAmount))
            .addOnSuccessListener {
                homeFragment.setAdGenerated(finalAmount)
            }
    }

    private fun displayLoading(isDisplay: Boolean) {
        binding.commonLoading.visibility = if(isDisplay) View.VISIBLE else View.GONE
    }
}