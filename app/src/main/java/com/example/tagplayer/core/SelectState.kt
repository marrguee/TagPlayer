package com.example.tagplayer.core

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.RadioGridState

class SelectState : View.BaseSavedState {
    private var selected: RadioGridState = RadioGridState.Empty

    fun save(selected: RadioGridState) {
        this.selected = selected
    }

    fun restore(restoreParcelable: SaveAndRestoreParcelable<RadioGridState>){
        restoreParcelable.restore(selected)
    }

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        selected = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                parcelIn.readParcelable(
                    RadioGridState.SelectChild::class.java.classLoader,
                    RadioGridState.SelectChild::class.java
                )
            } else {
                parcelIn.readParcelable(RadioGridState.SelectChild::class.java.classLoader)
            } as RadioGridState.SelectChild
        } catch (e: Exception) {
            RadioGridState.Empty
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeParcelable(selected, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SelectState?>{
        override fun createFromParcel(source: Parcel): SelectState = SelectState(source)
        override fun newArray(size: Int): Array<SelectState?> = arrayOfNulls(size)
    }
}