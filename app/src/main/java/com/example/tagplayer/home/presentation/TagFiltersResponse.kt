package com.example.tagplayer.home.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.example.tagplayer.filter_by_tags.presentation.TagFilterUi

interface TagFiltersResponse : Parcelable {

    fun map(mapper: TagFilterMapper)
    fun mapIntoAllList(allList: MutableList<TagFilterUi>)

    @SuppressLint("ParcelCreator")
    object Empty : TagFiltersResponse {
        override fun map(mapper: TagFilterMapper) = Unit
        override fun mapIntoAllList(allList: MutableList<TagFilterUi>) = Unit
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}

        @JvmField
        val CREATOR = object : Parcelable.Creator<Empty> {
            override fun createFromParcel(parcel: Parcel) = Empty
            override fun newArray(size: Int): Array<Empty?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    object EmptyList : TagFiltersResponse {
        override fun map(mapper: TagFilterMapper) = mapper.mapEmptyList()
        override fun mapIntoAllList(allList: MutableList<TagFilterUi>) = Unit
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}

        @JvmField
        val CREATOR = object : Parcelable.Creator<EmptyList> {
            override fun createFromParcel(parcel: Parcel) = EmptyList
            override fun newArray(size: Int): Array<EmptyList?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    class FilledList(
        private val list: List<Long>
    ) : TagFiltersResponse {
        override fun map(mapper: TagFilterMapper) = mapper.mapFilledList(list)
        override fun mapIntoAllList(allList: MutableList<TagFilterUi>) {
            allList.forEach { tag ->
                list.forEach { id ->
                    if (tag.compare(id)){
                        tag.changeSelected()
                    }
                }
            }
        }
        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeList(list)
        }

        companion object CREATOR : Parcelable.Creator<FilledList> {
            override fun createFromParcel(parcel: Parcel): FilledList {
                val list = mutableListOf<Long>()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    parcel.readList(list, Long::class.java.classLoader, Long::class.java)
                } else {
                    parcel.readList(list, Long::class.java.classLoader)
                }
                return FilledList(list)
            }

            override fun newArray(size: Int): Array<FilledList?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    class Error(private val error: String) : TagFiltersResponse {
        override fun map(mapper: TagFilterMapper) = mapper.mapError(error)
        override fun mapIntoAllList(allList: MutableList<TagFilterUi>) = Unit
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(error)
        }

        companion object CREATOR : Parcelable.Creator<Error> {
            override fun createFromParcel(parcel: Parcel): Error {
                val error = parcel.readString() ?: ""
                return Error(error)
            }
            override fun newArray(size: Int): Array<Error?> = arrayOfNulls(size)
        }
    }
}

