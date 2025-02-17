package com.example.tagplayer.tag_settings.presentation

import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.MyView
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuWithoutListener
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.SelectColor
import com.google.android.material.textfield.TextInputEditText
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagSettingsUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUiMenuWithoutListener, Parcelable {

    suspend fun provideId(
        title: String,
        color: String,
        block: suspend (String, String, Long) -> Unit
    ) {
        block.invoke(title, color, id)
    }

    fun fillFields(editText: TextInputEditText, selectColor: SelectColor) {
        editText.setText(title)
        selectColor.select(color)
    }

    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editTagMenu) action.action(this)
        else if (menuId == R.id.removeTagMenu) action.action(id)
    }

    override fun type(): ItemUiMenuType = ItemUiMenuType.TagSettingsMenuType

    override fun bind(vararg views: MyView) {
        views[0].apply {
            title(title)
            color(R.drawable.background_tag_oval, color)
        }
    }

    override fun same(other: CompareContent): Boolean = other.compare(id)

    override fun compare(otherId: Long): Boolean = otherId == id

    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = false
}