package com.funtease.practice

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.funtease.practice.adapters.JobAdapter
import com.funtease.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stUpJobRecyclerView()
    }

    private fun stUpJobRecyclerView() {
        val jobList = ArrayList<String>()
        jobList.add("Hello")
        jobList.add("Hello")
        jobList.add("Hello")
        jobList.add("Hello")
        val linearLayoutManager = LinearLayoutManager(this)
        with(binding.sideList) {
            layoutManager = linearLayoutManager
            adapter = JobAdapter(jobList)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                // Assumes current activity is the searchable activity
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                isIconifiedByDefault = false // Do not iconify the widget; expand it by default
            }
        }

        return true
    }
}