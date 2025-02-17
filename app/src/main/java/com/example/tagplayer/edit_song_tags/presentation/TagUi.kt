package com.example.tagplayer.edit_song_tags.presentation

import android.content.ClipData
import android.os.Build
import android.os.Parcelable
import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.edit_song_tags.domain.TagDomain
import com.example.tagplayer.core.CustomTextView
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.core.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUi, Parcelable {

    fun mapToDomain(): TagDomain = TagDomain(id, title, color)

    override fun type(): ItemUiType = ItemUiType.TagType

    override fun bind(vararg views: MyView) {
        views[0].let {
            it.title(title)
            it.color(R.drawable.background_tag_oval, color)
            (it as CustomTextView).setOnLongClickListener { view ->
                val clipData = ClipData.newPlainText(id.toString(), id.toString())
                val shadow = View.DragShadowBuilder(view)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(clipData, shadow, null, 0)
                }
                true
            }
        }

    }

    override fun same(other: CompareContent): Boolean = other.compare(id)
    override fun compare(otherId: Long): Boolean = otherId == id
    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = false
}