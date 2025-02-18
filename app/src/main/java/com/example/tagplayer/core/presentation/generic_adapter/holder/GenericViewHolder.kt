package com.example.tagplayer.core.presentation.generic_adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.tags_attach.presentation.TagUi
import com.example.tagplayer.core.presentation.custom_views.CustomTextView
import com.example.tagplayer.playback.presentation.TagPlaybackUi

abstract class GenericViewHolder<T : com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUi>(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T)

    class TagHolderListener(root: View) : GenericViewHolder<TagUi>(root) {
        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)

        override fun bind(item: TagUi) {
            item.bind(tagTextView)
        }
    }

    class TagHolder(root: View) : GenericViewHolder<TagPlaybackUi>(root) {
        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)

        override fun bind(item: TagPlaybackUi) {
            item.bind(tagTextView)
        }
    }
}