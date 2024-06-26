package com.example.tagplayer.history.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tagplayer.R

class HistoryRecyclerAdapter(
    private val listener: (Long) -> Unit,
) : ListAdapter<HistoryUi, HistoryRecyclerAdapter.HistoryViewHolder>(HistoryDiffUtil) {
    abstract class HistoryViewHolder(root: View) : ViewHolder(root) {
        abstract fun bind(item: HistoryUi, listener: (Long) -> Unit)

        class Song(root: View) : HistoryViewHolder(root) {
            private val titleView: TextView = root.findViewById(R.id.titleTextView)
            private val durationView: TextView = root.findViewById(R.id.durationTextView)

            override fun bind(item: HistoryUi, listener: (Long) -> Unit) {
                item.bind(titleView, durationView)
                itemView.setOnClickListener { item.tap(listener) }
            }
        }

        class Date(root: View) : HistoryViewHolder(root) {
            private val dateTextView: TextView = root.findViewById(R.id.dateTextView)

            override fun bind(item: HistoryUi, listener: (Long) -> Unit) {
                item.bind(dateTextView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val viewBlock: (Int) -> View = {
            LayoutInflater.from(parent.context)
                .inflate(it, parent, false)
        }
        return if (viewType == SONG_TYPE) {
            HistoryViewHolder.Song(viewBlock.invoke(R.layout.track_item))
        } else {
            HistoryViewHolder.Date(viewBlock.invoke(R.layout.date_holder_item))
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(currentList[position], listener)
    }

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position] is HistoryUi.Song) SONG_TYPE
        else HOLDER_TYPE
    }

    companion object {
        private const val SONG_TYPE = 1
        private const val HOLDER_TYPE = 2
    }
}