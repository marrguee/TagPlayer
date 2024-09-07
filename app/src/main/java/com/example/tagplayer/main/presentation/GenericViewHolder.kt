package com.example.tagplayer.main.presentation

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.recently.presentation.RecentlyUi.RecentlySongUi
import com.example.tagplayer.recently.presentation.RecentlyUi.DateUi
import com.example.tagplayer.tagsettings.presentation.MenuAction
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi

abstract class GenericViewHolder<T : ItemUiSimple>(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T, listener: (Long) -> Unit)

    abstract class GenericListenerViewHolder<T : ItemUiSimple>(root: View) : GenericViewHolder<T>(root)

//    class TagHolder(root: View) : GenericViewHolder<TagUi>(root) {
//        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)
//
//        override fun bind(item: TagUi, listener: (Long) -> Unit) {
//            item.bind(tagTextView)
//        }
//    }


    class LibraryHolder(root: View) : GenericViewHolder<SongUi>(root) {
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)
        override fun bind(item: SongUi, listener: (Long) -> Unit) {
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class RecentlyHolder(root: View) : GenericViewHolder<RecentlySongUi>(root) {
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)
        override fun bind(item: RecentlySongUi, listener: (Long) -> Unit) {
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class RecentlyDateHolder(root: View) : GenericViewHolder<DateUi>(root) {
        private val dateTextView: CustomTextView = itemView.findViewById(R.id.dateTextView)

        override fun bind(item: DateUi, listener: (Long) -> Unit) {
            item.bind(dateTextView)
        }
    }
}

abstract class GenericMenuViewHolder<T : ItemUiMenu>(
    root: View
) : RecyclerView.ViewHolder(root), View.OnCreateContextMenuListener {
    protected val popupMenu = PopupMenu(itemView.context, itemView)
    abstract fun bind(item: T, menuOptions: List<Pair<Int, MenuAction>>)

    class TagSettingsHolderMenu(
        root: View
    ) : GenericMenuViewHolder<TagSettingsUi>(root) {

        init {
            popupMenu.inflate(R.menu.popup_menu_tag_settings)
            itemView.setOnLongClickListener {
                popupMenu.show()
                true
            }
        }

        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)

        override fun bind(item: TagSettingsUi, menuOptions: List<Pair<Int, MenuAction>>) {
            item.bind(tagTextView)
            popupMenu.setOnMenuItemClickListener { itd ->
                menuOptions.firstOrNull { itd.itemId == it.first }?.let { block ->
                    item.popup(itd.itemId, block.second)
                    true
                }
                false
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(itemView.context)
            inflater.inflate(R.menu.popup_menu_tag_settings, menu)
        }


    }
}