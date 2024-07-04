package com.example.tagplayer.search.presentation

import com.example.tagplayer.main.presentation.GenericAdapter
import com.example.tagplayer.main.presentation.ItemUiType

class SearchAdapter(
    listener: (Long) -> Unit
) : GenericAdapter.ItemUiAdapter(
    listener,
    listOf(ItemUiType.SongType)
)
//class SearchAdapter(
//    private val listener: (Long) -> Unit
//) : ListAdapter<SongUi, SearchAdapter.TrackHolder>(AllDiffUtil) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
//        val view = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.track_item, parent, false)
//        return TrackHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TrackHolder, pos: Int) {
//        holder.bind(currentList[pos], listener)
//    }
//}