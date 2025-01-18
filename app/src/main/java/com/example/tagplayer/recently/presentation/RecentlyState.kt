package com.example.tagplayer.recently.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.home.presentation.TagFiltersState.EmptyList

interface RecentlyState : Parcelable {
    fun dispatch(recyclerView: RecyclerView)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    @SuppressLint("ParcelCreator")
    class RecentlyUpdated(private val list: List<RecentlyUi>) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) {
            (recyclerView.adapter as RecentlyListenerAdapter).submitList(list)
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}

        @JvmField
        val CREATOR = object : Parcelable.Creator<RecentlyUpdated> {
            override fun createFromParcel(parcel: Parcel) : RecentlyUpdated {
                val list = mutableListOf<RecentlyUi>()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    parcel.readList(list, RecentlyUi::class.java.classLoader, RecentlyUi::class.java)
                } else {
                    parcel.readList(list, RecentlyUi::class.java.classLoader)
                }
                return RecentlyUpdated(list)
            }
            override fun newArray(size: Int): Array<RecentlyUpdated?> = arrayOfNulls(size)
        }

    }
    @SuppressLint("ParcelCreator")
    class Error(private val cause: String) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) = Unit
        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}

        companion object CREATOR : Parcelable.Creator<Error> {
            override fun createFromParcel(parcel: Parcel): Error {
                val error = parcel.readString() ?: ""
                return Error(error)
            }
            override fun newArray(size: Int): Array<Error?> = arrayOfNulls(size)
        }
    }
    @SuppressLint("ParcelCreator")
    object Empty : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) = Unit
        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {}

        @JvmField
        val CREATOR = object : Parcelable.Creator<EmptyList> {
            override fun createFromParcel(parcel: Parcel) = EmptyList
            override fun newArray(size: Int): Array<EmptyList?> = arrayOfNulls(size)
        }
    }
}