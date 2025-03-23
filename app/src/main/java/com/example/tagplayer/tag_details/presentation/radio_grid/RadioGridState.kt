package com.example.tagplayer.tag_details.presentation.radio_grid

import android.os.Parcelable
import android.view.View
import com.example.tagplayer.core.presentation.custom_views.interfaces.SelectAndUnselect
import kotlinx.parcelize.Parcelize
@Parcelize
sealed interface RadioGridState : Parcelable {
    fun dispatch(children: Sequence<View>)

    @Parcelize
    class SelectChildById(private val index: Int) : RadioGridState {
        override fun dispatch(children: Sequence<View>) {
            children.forEach {
                (it as SelectAndUnselect).unselect()
            }
            (children.toList()[index] as SelectAndUnselect).select()
        }
    }

    @Parcelize
    class SelectChildByColor(private val color: String) : RadioGridState {
        override fun dispatch(children: Sequence<View>) {
            children.forEach {
                (it as SelectAndUnselect).unselect()
            }
            try {
                children.first { (it as ProvideColor).color() == color }
            } catch (e: Exception) {
                null
            }?.let {
                (it as SelectAndUnselect).select()
                return
            }
            (children.toList().first() as SelectAndUnselect).select()
        }
    }

    @Parcelize
    data object Empty : RadioGridState {
        override fun dispatch(
            children: Sequence<View>
        ) = Unit
    }
}