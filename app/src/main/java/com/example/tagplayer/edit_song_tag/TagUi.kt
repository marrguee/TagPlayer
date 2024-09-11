package com.example.tagplayer.edit_song_tag

import android.content.ClipData
import android.os.Build
import android.view.View
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

data class TagUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUi {

    fun mapToDomain(): TagDomain = TagDomain(id, title, color)

    override fun type(): ItemUiType = ItemUiType.TagType

    override fun bind(vararg views: MyView) {
        views[0].title(title)
        (views[0] as CustomTextView).setOnLongClickListener {
            val clipData = ClipData.newPlainText("Id", id.toString())
            val shadow = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(clipData, shadow, null, 0)
            }
            true
        }
    }

    override fun same(other: CompareContent): Boolean = other.compare(id)

    override fun compare(otherId: Long): Boolean = otherId == id

    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = false

}