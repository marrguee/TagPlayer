package com.example.tagplayer.all.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R

class AllFragment : Fragment(R.layout.all_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: List<TrackUi> = listOf(
            TrackUi("First", "4:13"),
            TrackUi("Second", "4:13"),
            TrackUi("Third", "4:13")
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.trackRecycler)
        recyclerView.adapter = AllRecyclerAdapter().apply {
            update(list)
        }
    }
}