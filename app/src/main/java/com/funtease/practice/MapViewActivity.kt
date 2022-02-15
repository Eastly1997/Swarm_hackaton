package com.funtease.practice

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.funtease.practice.databinding.ActivityMapViewBinding

class MapViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapViewBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}