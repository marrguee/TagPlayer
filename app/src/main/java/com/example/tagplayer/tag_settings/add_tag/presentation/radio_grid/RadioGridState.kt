package com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid

import android.os.Parcelable
import android.view.View
import com.example.tagplayer.core.SelectAndUnselect
import kotlinx.parcelize.Parcelize
@Parcelize
sealed interface RadioGridState : Parcelable {
    fun dispatch(children: Sequence<View>)

    @Parcelize
    class SelectChild(private val index: Int) : RadioGridState {
        override fun dispatch(children: Sequence<View>) {
            children.forEach {
                (it as SelectAndUnselect).unselect()
            }
            (children.toList()[index] as SelectAndUnselect).select()
        }
    }

    @Parcelize
    data object Empty : RadioGridState {
        override fun dispatch(
            children: Sequence<View>
        ) = Unit
    }
}