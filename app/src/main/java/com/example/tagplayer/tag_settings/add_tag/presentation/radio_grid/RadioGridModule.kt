package com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.Module

class RadioGridModule : Module<RadioGridViewModel> {
    private val observable: CustomObservable.ParcelableStateHandleManualClear<RadioGridState> =
        RadioGridObservable()
    override fun create(): RadioGridViewModel {
        return RadioGridViewModel(observable)
    }
}