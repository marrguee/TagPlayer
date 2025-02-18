package com.example.tagplayer.recently.presentation

import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentlyUi(
    private val id: Long,
    private val thumbnail: String?,
    private val title: String,
    private val author: String,
    private val duration: String
) : ItemUiMenuListener, HandleTap<Long>, Parcelable {

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

    override fun type(): ItemUIMenuListenerType = ItemUIMenuListenerType.RecentlyMenuType

    override fun same(other: CompareContent) = other.compare(id)
    override fun compare(otherId: Long) = id == otherId
    override fun compare(otherDate: String) = false
    override fun compare(otherBoolean: Boolean): Boolean = false
}

