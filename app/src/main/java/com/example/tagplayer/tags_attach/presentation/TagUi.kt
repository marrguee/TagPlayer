package com.example.tagplayer.tags_attach.presentation

import android.content.ClipData
import android.os.Build
import android.os.Parcelable
import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.presentation.custom_views.CustomTextView
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUi
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUi, Parcelable {

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, title, color)

    interface Mapper<T> {
        fun map(id: Long, title: String, color: String): T

        object Id: Mapper<Long> {
            override fun map(id: Long, title: String, color: String): Long = id
        }
    }

    override fun bind(vararg views: MyView) {
        views[0].let {
            it.title(title)
            it.color(R.drawable.background_tag, color)
            (it as CustomTextView).setOnLongClickListener { view ->
                val clipData = ClipData.newPlainText(id.toString(), id.toString())
                val shadow = View.DragShadowBuilder(view)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(clipData, shadow, null, 0)
                } else {
                    @Suppress("DEPRECATION")
                    view.startDrag(clipData, shadow, null, 0)
                }
                true
            }
        }
    }

    override fun type(): ItemUiType = ItemUiType.TagType

    override fun same(other: CompareContent): Boolean = other.compare(id)
    override fun compare(otherId: Long): Boolean = otherId == id
    override fun compare(otherDate: String): Boolean = false
    override fun compare(otherBoolean: Boolean): Boolean = false
}