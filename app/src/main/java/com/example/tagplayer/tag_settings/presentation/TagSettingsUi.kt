package com.example.tagplayer.tag_settings.presentation

import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuType
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuWithoutListener
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagSettingsUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUiMenuWithoutListener, Parcelable {

    override fun bind(vararg views: MyView) {
        views[0].apply {
            title(title)
            color(R.drawable.background_tag, color)
        }
    }

    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editTagMenu || menuId == R.id.removeTagMenu)
            action.action(id)
    }

    override fun type(): ItemUiMenuType = ItemUiMenuType.TagSettingsMenuType

    override fun same(other: CompareContent): Boolean = other.compare(id)
    override fun compare(otherId: Long): Boolean = otherId == id
    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = false
}