package com.example.tagplayer.tagsettings.presentation

import androidx.lifecycle.MutableLiveData
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.ItemUiMenu
import com.example.tagplayer.main.presentation.ItemUiTypeWithInit
import com.example.tagplayer.main.presentation.MyView
import com.google.android.material.textfield.TextInputEditText

data class TagSettingsUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUiMenu {

    suspend fun provideId(
        title: String,
        color: String,
        block: suspend (String, String, Long) -> Unit
    ) {
        block.invoke(title, color, id)
    }

    fun fillFields(editText: TextInputEditText) {
        editText.setText(title)
    }

    override fun popup(menuId: Int, action: MenuAction) {
        if (menuId == R.id.editTagMenu) action.action(this)
        else if (menuId == R.id.removeTagMenu) action.action(id)
    }

    override fun type(): ItemUiTypeWithInit = ItemUiTypeWithInit.TagSettingsType

    override fun bind(vararg views: MyView) {
        views[0].title(title)
    }

    override fun same(other: CompareContent): Boolean = other.compare(id)

    override fun compare(otherId: Long): Boolean = otherId == id

    override fun compare(otherDate: String): Boolean = false
}