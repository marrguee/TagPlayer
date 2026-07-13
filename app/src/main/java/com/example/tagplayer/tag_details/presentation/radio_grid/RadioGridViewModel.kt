package com.example.tagplayer.tag_details.presentation.radio_grid

import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.save_restore.SaveAndRestoreParcelable
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel

class RadioGridViewModel(
    private val observable: CustomObservable.ParcelableStateHandleManualClear<RadioGridState>
) : ComebackViewModel(),
    HandleUiStateUpdates.StartAndStopUpdates<RadioGridState>,
    SaveAndRestoreParcelable<RadioGridState>
{
    fun init() = observable.update(
        RadioGridState.SelectChildById(
            0
        )
    )

    fun selectAt(index: Int) = observable.update(
        RadioGridState.SelectChildById(
            index
        )
    )

    fun selectByColor(color: String) = observable.update(
        RadioGridState.SelectChildByColor(
            color
        )
    )

    override fun startGettingUpdates(observer: CustomObserver<RadioGridState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(RadioGridObserver.Empty)

    override fun save(): RadioGridState = observable.save()

    override fun restore(data: RadioGridState) = observable.restore(data)
}