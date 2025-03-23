package com.example.tagplayer.search.presentation

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuListener

data class SearchUi(
    private val id: Long,
    private val thumbnail: String?,
    private val title: String,
    private val author: String,
    private val duration: String
) : ItemUiMenuListener, HandleTap<Long> {

    override fun tap(listener: (Long) -> Unit) = listener.invoke(id)

    override fun bind(vararg views: MyView) {
        views[0].image(thumbnail)
        views[1].title(title)
        views[2].title(author)
        views[3].title(duration)
    }

    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editSongTagsMenu) action.action(id)
    }

    override fun type(): ItemUIMenuListenerType = ItemUIMenuListenerType.SearchMenuType

    override fun same(other: CompareContent) = other.compare(title)
    override fun toString() = title
    override fun compare(otherId: Long) = false
    override fun compare(otherDate: String) = otherDate == title
    override fun compare(otherBoolean: Boolean): Boolean = false
}