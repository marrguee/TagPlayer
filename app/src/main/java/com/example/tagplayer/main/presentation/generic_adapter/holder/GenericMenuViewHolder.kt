package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenu
import com.example.tagplayer.tagsettings.presentation.MenuAction
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi

abstract class GenericMenuViewHolder<T : ItemUiMenu>(
    root: View,
    private val menuLayoutRes: Int
) : RecyclerView.ViewHolder(root), View.OnCreateContextMenuListener {
    abstract val viewForPopup: View
    abstract val setPopupListenerBlock: () -> Unit

    protected val popupMenu = PopupMenu(itemView.context, itemView).apply {
        inflate(menuLayoutRes)
    }

    open fun bind(item: T, menuOptions: List<Pair<Int, MenuAction>>) {
        setPopupListenerBlock.invoke()
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
        val inflater = MenuInflater(viewForPopup.context)
        inflater.inflate(menuLayoutRes, menu)
    }

    class TagSettingsHolderMenu(
        root: View
    ) : GenericMenuViewHolder<TagSettingsUi>(root, R.menu.popup_menu_tag_settings) {
        override val viewForPopup: View = itemView
        override val setPopupListenerBlock: () -> Unit = {
            itemView.setOnLongClickListener {
                popupMenu.show()
                true
            }
        }

        private val tagTextView: CustomTextView = itemView.findViewById(R.id.tagTextView)

        override fun bind(item: TagSettingsUi, menuOptions: List<Pair<Int, MenuAction>>) {
            super.bind(item, menuOptions)
            item.bind(tagTextView)
        }

    }


}