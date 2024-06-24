package com.example.tagplayer.all.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R

class AllRecyclerAdapter(
    private val listener: (Long) -> Unit
) : RecyclerView.Adapter<AllRecyclerAdapter.TrackHolder>() {
    private var list: MutableList<SongUi> = mutableListOf()
    class TrackHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val titleView: TextView = root.findViewById(R.id.titleTextView)
        private val durationView: TextView = root.findViewById(R.id.durationTextView)

        fun bind(item: SongUi, listener: (Long) -> Unit) {
            item.bind(titleView, durationView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(holder: TrackHolder, pos: Int) {
        holder.bind(list[pos], listener)
    }

    fun update(data: List<SongUi>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}