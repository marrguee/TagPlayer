package com.example.tagplayer.tag_details.presentation.radio_grid

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.ClearViewModel

class RadioGridModule(
    private val clear: ClearViewModel
) : Module<RadioGridViewModel> {
    private val observable: CustomObservable.ParcelableStateHandleManualClear<RadioGridState> =
        RadioGridObservable()
    override fun create(): RadioGridViewModel {
        return RadioGridViewModel(
            clear,
            observable
        )
    }
}