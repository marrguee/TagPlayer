package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.View
import android.widget.ImageButton
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.tagsettings.presentation.MenuAction

abstract class GenericMenuListenerViewHolder<T : ItemUiMenuListener, L>(
    root: View,
    menuLayoutRes: Int
) : GenericMenuViewHolder<T>(root, menuLayoutRes) {

    open fun bind(item: T, menuOptions: List<Pair<Int, MenuAction>>, listener: (L) -> Unit) {
        super.bind(item, menuOptions)
    }

    class HomeSongHolderMenu(
        root: View
    ) : GenericMenuListenerViewHolder<SongUi, Long>(root, R.menu.home_song_menu) {

        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)
        private val songMenuButton: ImageButton = itemView.findViewById(R.id.songMenuButton)

        override val viewForPopup: View = songMenuButton
        override val setPopupListenerBlock: () -> Unit = {
            songMenuButton.setOnClickListener {
                popupMenu.show()
            }
        }


        override fun bind(item: SongUi, menuOptions: List<Pair<Int, MenuAction>>, listener: (Long) -> Unit) {
            super.bind(item, menuOptions, listener)
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }

    }


}