package com.example.tagplayer.core.presentation.save_restore

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.example.tagplayer.tag_details.presentation.radio_grid.RadioGridState

class SelectState : View.BaseSavedState {
    private var selected: RadioGridState = RadioGridState.Empty

    fun save(selected: RadioGridState) {
        this.selected = selected
    }

    fun restore(restoreParcelable: SaveAndRestoreParcelable<RadioGridState>) {
        restoreParcelable.restore(selected)
    }

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        selected = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                parcelIn.readParcelable(
                    RadioGridState.SelectChildById::class.java.classLoader,
                    RadioGridState.SelectChildById::class.java
                )
            } else {
                parcelIn.readParcelable(RadioGridState.SelectChildById::class.java.classLoader)
            } as RadioGridState.SelectChildById
        } catch (e: Exception) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    parcelIn.readParcelable(
                        RadioGridState.SelectChildByColor::class.java.classLoader,
                        RadioGridState.SelectChildByColor::class.java
                    )
                } else {
                    parcelIn.readParcelable(RadioGridState.SelectChildByColor::class.java.classLoader)
                } as RadioGridState.SelectChildByColor
            } catch (e: Exception) {
                RadioGridState.Empty
            }
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeParcelable(selected, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SelectState?> {
        override fun createFromParcel(source: Parcel): SelectState = SelectState(source)
        override fun newArray(size: Int): Array<SelectState?> = arrayOfNulls(size)
    }
}