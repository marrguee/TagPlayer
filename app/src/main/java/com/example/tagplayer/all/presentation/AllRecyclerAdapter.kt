package com.example.tagplayer.all.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R

class AllRecyclerAdapter : RecyclerView.Adapter<AllRecyclerAdapter.TrackHolder>() {
    private var list: MutableList<TrackUi> = mutableListOf()
    class TrackHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val titleView: TextView = root.findViewById(R.id.titleTextView)
        private val durationView: TextView = root.findViewById(R.id.durationTextView)

        fun bind(item: TrackUi) = item.bind(titleView, durationView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(holder: TrackHolder, pos: Int) {
        holder.bind(list[pos])
    }

    fun update(data: List<TrackUi>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}