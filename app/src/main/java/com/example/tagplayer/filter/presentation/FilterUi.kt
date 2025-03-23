package com.example.tagplayer.filter.presentation

import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUiListener
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiListenerType
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterUi(
    private val id: Long,
    private val title: String,
    private val color: String,
    private var selected: Boolean = false
) : ItemUiListener, HandleTap<Pair<Long, Boolean>>, Parcelable {

    override fun tap(listener: (Pair<Long, Boolean>) -> Unit) {
        listener.invoke(Pair(id, !selected))
    }

    override fun bind(vararg views: MyView) {
        views[0].apply {
            title(title)
            color(R.drawable.background_tag_oval, if (selected) String() else color)
        }
    }

    override fun type(): ItemUiListenerType = ItemUiListenerType.TagFilterListenerType

    override fun same(other: CompareContent): Boolean = other.compare(id) && other.compare(selected)
    override fun compare(otherId: Long): Boolean = otherId == id
    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = otherBoolean == selected
}
