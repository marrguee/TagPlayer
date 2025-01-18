package com.example.tagplayer.filter_by_tags.presentation

import android.os.Parcel
import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.filter_by_tags.domain.TagFilterDomain
import com.example.tagplayer.main.presentation.ItemUiListener
import com.example.tagplayer.main.presentation.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

data class FilterUi(
    private val id: Long,
    private val title: String,
    private val color: String,
    private var selected: Boolean = false
) : ItemUiListener, HandleTap, ChangeSelectedState, Parcelable {

    fun map() = TagFilterDomain(id, title, color)

    override fun compare(otherId: Long): Boolean =
        otherId == id

    override fun same(other: CompareContent): Boolean =
        other.compare(id) && other.compare(selected)

    override fun compare(otherDate: String): Boolean =
        false

    override fun compare(otherBoolean: Boolean): Boolean =
        otherBoolean == selected

    override fun bind(vararg views: MyView) {
        views[0].apply {
            title(title)
            val aColor = if (selected) R.drawable.oval_background_selected else R.drawable.oval_background
            color(aColor)
        }
    }

    override fun type(): ItemUiListenerType = ItemUiListenerType.TagFilterListenerType

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun changeSelected() {
        selected = !selected
    }

    fun selected() = selected
    fun id() = id

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(color)
        dest.writeByte(if (selected) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<FilterUi> {
        override fun createFromParcel(parcel: Parcel): FilterUi {
            val id = parcel.readLong()
            val title = parcel.readString() ?: ""
            val color = parcel.readString() ?: ""
            val selected = parcel.readByte() != 0.toByte()
            return FilterUi(id, title, color, selected)
        }

        override fun newArray(size: Int): Array<FilterUi?> = arrayOfNulls(size)
    }
}
