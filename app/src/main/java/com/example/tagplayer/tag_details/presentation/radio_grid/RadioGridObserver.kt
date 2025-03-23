package com.example.tagplayer.tag_details.presentation.radio_grid

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface RadioGridObserver : CustomObserver<RadioGridState> {
    object Empty : RadioGridObserver {
        override fun update(data: RadioGridState) = Unit
    }
}