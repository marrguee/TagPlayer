package com.example.tagplayer.tag_details.presentation

import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen

abstract class TagViewModel(
    clear: ClearViewModel,
    private val observable: CustomObservable.All<TagDialogState>,
    private val handleDeath: HandleDeath,
) : ComebackViewModel(clear), HandleUiStateUpdates.All<TagDialogState> {
    protected abstract val initBlock : (Long?) -> Unit
    abstract fun accept(title: String, color: String, dismissCallback: () -> Unit)

    fun init(id: Long? = null) {
        if (handleDeath.deathHappened()) {
            initBlock.invoke(id)
            handleDeath.handleDeath()
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<TagDialogState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(TagDetailsObserver.Empty)

    override fun clear() = observable.clear()

}