package com.example.tagplayer.core.presentation.generic_adapter.holder

import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.CustomShapeableImageView
import com.example.tagplayer.core.presentation.custom_views.CustomTextView
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.home.presentation.SongUi
import com.example.tagplayer.recently.presentation.RecentlyUi
import com.example.tagplayer.search.presentation.SearchUi

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
        private val thumbnailImageView: CustomShapeableImageView =
            itemView.findViewById(R.id.thumbnailImageView)
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: CustomTextView = itemView.findViewById(R.id.authorTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)

        override fun bind(
            item: SongUi,
            menuOptions: List<Pair<Int, MenuAction>>,
            listener: (Long) -> Unit
        ) {
            super.bind(item, menuOptions, listener)
            item.bind(thumbnailImageView, titleTextView, authorTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class SearchHolderMenu(
        root: View
    ) : GenericMenuListenerViewHolder<SearchUi, Long>(
        root,
        R.id.songMenuButton,
        R.menu.home_song_menu
    ) {
        private val thumbnailImageView: CustomShapeableImageView =
            itemView.findViewById(R.id.thumbnailImageView)
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: CustomTextView = itemView.findViewById(R.id.authorTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)

        override fun bind(
            item: SearchUi,
            menuOptions: List<Pair<Int, MenuAction>>,
            listener: (Long) -> Unit
        ) {
            super.bind(item, menuOptions, listener)
            item.bind(thumbnailImageView, titleTextView, authorTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class RecentlyHolderMenu(
        root: View
    ) : GenericMenuListenerViewHolder<RecentlyUi, Long>(
        root,
        R.id.songMenuButton,
        R.menu.home_song_menu
    ) {
        private val thumbnailImageView: CustomShapeableImageView =
            itemView.findViewById(R.id.thumbnailImageView)
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: CustomTextView = itemView.findViewById(R.id.authorTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)

        override fun bind(
            item: RecentlyUi,
            menuOptions: List<Pair<Int, MenuAction>>,
            listener: (Long) -> Unit
        ) {
            super.bind(item, menuOptions, listener)
            item.bind(thumbnailImageView, titleTextView, authorTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

}