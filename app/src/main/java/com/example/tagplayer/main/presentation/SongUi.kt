package com.example.tagplayer.main.presentation

import com.example.tagplayer.R
import com.example.tagplayer.core.MyView
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.tag_settings.presentation.MenuAction

data class SongUi(
    private val id: Long,
    private val thumbnail: String?,
    private val title: String,
    private val author: String,
    private val duration: String
) : ItemUiMenuListener, HandleTap {

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun type() = ItemUIMenuListenerType.HomeSongMenuType

    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editSongTagsMenu) action.action(id)
    }

    override fun bind(vararg views: MyView) {
        views[0].image(thumbnail)
        views[1].title(title)
        views[2].title(author)
        views[3].title(duration)
    }

    override fun same(other: CompareContent) = other.compare(id)

    override fun compare(otherId: Long) = otherId == id
    override fun compare(otherDate: String) = false
    override fun compare(otherBoolean: Boolean): Boolean = false


}