package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.edit_song_tag.TagUi
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.ItemUi

abstract class GenericViewHolder<T : ItemUi>(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T)

    class TagHolderListener(root: View) : GenericViewHolder<TagUi>(root) {
        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)

        override fun bind(item: TagUi) {
            item.bind(tagTextView)
        }
    }
}