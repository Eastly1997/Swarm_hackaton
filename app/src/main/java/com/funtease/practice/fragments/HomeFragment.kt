package com.funtease.practice.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.funtease.practice.MainActivity
import com.funtease.practice.R
import com.funtease.practice.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity:MainActivity
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>
    private lateinit var searchText: EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val spokenText: String? =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                        results!![0]
                    }
                searchText.setText(spokenText)
                //TODO : Search
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        setUpSearchBar()
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
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            }
            // This starts the activity and populates the intent with the speech text.
            launchSomeActivity.launch(intent)
        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                voice.visibility = if(s.isEmpty()) View.VISIBLE else View.GONE
                close.visibility = if(s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }



}