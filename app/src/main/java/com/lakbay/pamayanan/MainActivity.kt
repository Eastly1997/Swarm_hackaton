package com.lakbay.pamayanan

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
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
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.Donation
import com.lakbay.pamayanan.viewModels.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchText: EditText
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>

    private val db = Firebase.firestore
    private val usersRef = db.collection("users")
    private val donateRef = db.collection("donation").document("Juan Earth")
    private var currentUser: User = User()
    private var currentDonation: Donation = Donation()
    var topList: ArrayList<User> = ArrayList<User>()

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
        getTotalDonation()
        getTopDonation(5)
        displayLoading(false)
    }

    private fun setUpSearchBar() {
        val searchBar: View = binding.root.findViewById(R.id.search_layout)
        val voice = searchBar.findViewById<ImageView>(R.id.search_voice)
        val close = searchBar.findViewById<ImageView>(R.id.search_close)
        searchText = searchBar.findViewById(R.id.search_text)

        close.setOnClickListener {
            Handler().postDelayed({
                searchText.text.clear()
            }, 180)
        }

        voice.setOnClickListener {

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
            }
            // This starts the activity and populates the intent with the speech text.
            launchSomeActivity.launch(intent)
        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                voice.visibility = if (s.isEmpty()) View.VISIBLE else View.GONE
                close.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            (menu.findItem(R.id.menu_search).actionView as android.widget.SearchView).apply {
                // Assumes current activity is the searchable activity
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                isIconifiedByDefault = false // Do not iconify the widget; expand it by default
            }
        }

        return true
    }

    private fun getUserData() {
        usersRef.document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            currentUser = it.toObject<User>()!!
            SharedPrefUtils.saveData(this, User.FIELD_USER_NAME, currentUser.userName)
            SharedPrefUtils.saveData(this, User.FIELD_DONATED_AMOUNT, currentUser.donatedAmount.toFloat())
            SharedPrefUtils.saveData(this, User.FIELD_UID, currentUser.uid)
            SharedPrefUtils.saveData(this, User.FIELD_IMG, currentUser.img)
            SharedPrefUtils.saveData(this, User.FIELD_MOBILE_NUMBER, currentUser.mobileNumber)
            homeFragment.individualAdGenerated = currentUser.donatedAmount
            homeFragment.binding.individualEarned = CommonUtils.convertToAmount(currentUser.donatedAmount)
        }
    }

    fun updateDonation(amount: Double) {
        Log.d("FIREBASE", "Update Amount: $amount")
        usersRef.document(currentUser.uid).update(User.FIELD_DONATED_AMOUNT, increment(amount * 0.75))
        donateRef.update(Donation.FIELD_CHARITY, increment(amount * 0.75),
            Donation.FIELD_COMMUNITY, increment(amount * 0.15),
            Donation.FIELD_DEVELOPMENT, increment(amount * 0.1),
            Donation.FIELD_TOTAL, increment(amount)).addOnSuccessListener {
            homeFragment.setlAdGenerated(amount * 0.75)
        }
    }

    private fun getTotalDonation() {
        donateRef.get().addOnSuccessListener {
            currentDonation = it.toObject<Donation>()!!
            SharedPrefUtils.saveData(this, Donation.FIELD_CHARITY, currentDonation.charity.toFloat())
            SharedPrefUtils.saveData(this, Donation.FIELD_DEVELOPMENT, currentDonation.development.toFloat())
            SharedPrefUtils.saveData(this, Donation.FIELD_COMMUNITY, currentDonation.community.toFloat())
            SharedPrefUtils.saveData(this, Donation.FIELD_TOTAL, currentDonation.total.toFloat())
            homeFragment.binding.totalEarned = CommonUtils.convertToAmount(currentDonation.charity)
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