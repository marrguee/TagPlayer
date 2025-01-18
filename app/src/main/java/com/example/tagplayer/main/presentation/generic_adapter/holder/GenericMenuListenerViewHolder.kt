package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.tag_settings.presentation.MenuAction

abstract class GenericMenuListenerViewHolder<T : ItemUiMenuListener, L>(
    root: View,
    viewIdPopupMenu: Int,
    menuLayoutRes: Int
) : GenericMenuViewHolder<T>(root, viewIdPopupMenu, menuLayoutRes) {

    open fun bind(item: T, menuOptions: List<Pair<Int, MenuAction>>, listener: (L) -> Unit) {
        super.bind(item, menuOptions)
    }

    class HomeSongHolderMenu(
        root: View
    ) : GenericMenuListenerViewHolder<SongUi, Long>(
        root,
        R.id.songMenuButton,
        R.menu.home_song_menu
    ) {
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)

        override fun bind(
            item: SongUi,
            menuOptions: List<Pair<Int, MenuAction>>,
            listener: (Long) -> Unit
        ) {
            super.bind(item, menuOptions, listener)
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }

    }


}