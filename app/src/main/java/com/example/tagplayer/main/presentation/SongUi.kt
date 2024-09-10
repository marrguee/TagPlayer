package com.example.tagplayer.main.presentation

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandlePlayTap
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.tagsettings.presentation.MenuAction

data class SongUi(
    private val id: Long,
    private val title: String,
    private val duration: String
) : ItemUiMenuListener, HandlePlayTap {

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun type() = ItemUIMenuListenerType.HomeSongMenuType


    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editSongTagsMenu) action.action(id)
    }

    override fun bind(vararg views: MyView) {
        views[0].title(title)
        views[1].title(duration)
    }

    override fun same(other: CompareContent) = other.compare(id)

    override fun compare(otherId: Long) = otherId == id
    override fun compare(otherDate: String) = false


}