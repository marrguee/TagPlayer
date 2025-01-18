package com.example.tagplayer.edit_song_tags.presentation

import android.content.ClipData
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.edit_song_tags.domain.TagDomain
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

data class TagUi(
    private val id: Long,
    private val title: String,
    private val color: String
) : ItemUi, Parcelable {

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

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(color)
    }

    companion object CREATOR : Parcelable.Creator<TagUi> {
        override fun createFromParcel(parcel: Parcel): TagUi {
            val id = parcel.readLong()
            val title = parcel.readString() ?: ""
            val color = parcel.readString() ?: ""
            return TagUi(id, title, color)
        }

        override fun newArray(size: Int): Array<TagUi?> = arrayOfNulls(size)
    }
}