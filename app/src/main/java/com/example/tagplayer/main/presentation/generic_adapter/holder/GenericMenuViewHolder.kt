package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenu
import com.example.tagplayer.tag_settings.presentation.MenuAction
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi

abstract class GenericMenuViewHolder<T : ItemUiMenu>(
    root: View,
    viewIdPopupMenu: Int,
    private val menuLayoutRes: Int,
) : RecyclerView.ViewHolder(root), View.OnCreateContextMenuListener {
    protected val viewForPopup: View = itemView.findViewById(viewIdPopupMenu)

    private val popupMenu = PopupMenu(viewForPopup.context, viewForPopup).apply {
        inflate(menuLayoutRes)
    }

    open fun bind(item: T, menuOptions: List<Pair<Int, MenuAction>>) {
        viewForPopup.setOnClickListener {
            popupMenu.show()
        }
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
    ) : GenericMenuViewHolder<TagSettingsUi>(
        root,
        R.id.tagTextView,
        R.menu.popup_menu_tag_settings,
    ) {

        private val tagTextView: CustomTextView = viewForPopup as CustomTextView

        override fun bind(item: TagSettingsUi, menuOptions: List<Pair<Int, MenuAction>>) {
            super.bind(item, menuOptions)
            item.bind(tagTextView)
        }

    }


}