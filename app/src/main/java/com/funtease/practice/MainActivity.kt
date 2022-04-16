package com.funtease.practice

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.funtease.practice.adapters.ViewPagerAdapter
import com.funtease.practice.databinding.ActivityMainBinding
import com.funtease.practice.fragments.HomeFragment
import com.funtease.practice.fragments.MapFragment
import com.funtease.practice.fragments.ProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchText: EditText
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>

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



}