package com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.RadioGridLayout
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.SaveAndRestoreParcelable

class RadioGridViewModel(
    private val observable: CustomObservable.ParcelableStateHandleManualClear<RadioGridState>
) : ViewModel(),
    HandleUiStateUpdates.StartAndStopUpdates<RadioGridState>,
    SaveAndRestoreParcelable<RadioGridState>
{

    fun init() {
        observable.update(RadioGridState.SelectChild(0))
    }

    fun selectAt(index: Int) {
        observable.update(RadioGridState.SelectChild(index))
    }

    override fun startGettingUpdates(observer: CustomObserver<RadioGridState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(RadioGridLayout.RadioGridObserver.Empty)
    }

    override fun save(): RadioGridState = observable.save()

    override fun restore(data: RadioGridState) {
        observable.restore(data)
    }
}