package com.funtease.practice.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.funtease.practice.adapters.JobAdapter
import com.funtease.practice.adapters.JobAdapterExplore
import com.funtease.practice.databinding.FragmentHomeBinding
import com.funtease.practice.utils.CenterZoomLinearLayoutManager


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        with(binding.jobList) {
            layoutManager = CenterZoomLinearLayoutManager(activity!!.baseContext, 1f, 0.175f)
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
            adapter = JobAdapter(jobList, width, activity!!)
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

        with(binding.jobListCommon) {
            layoutManager = LinearLayoutManager(activity)
            val jobList = ArrayList<String>()
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            jobList.add("SAMPLE")
            adapter = JobAdapterExplore(jobList)
        }
    }
}

