package com.example.tagplayer.filter.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.filter.domain.FilterInteractor
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.filter.domain.FilterResponse
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen

class FilterViewModel(
    clear: ClearViewModel,
    private val runAsync: RunAsync,
    private val observable: CustomObservable.AllHandleState<FilterState>,
    private val interactor: FilterInteractor,
    private val mapper: FilterResponse.Mapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath,
) : ComebackViewModel(clear), HandleUiStateUpdates.All<FilterState> {
    private val uiBlock: (FilterResponse) -> Unit = { it.map(mapper, viewModelScope) }

    fun init() {
        if (handleDeath.deathHappened()) {
            runAsync.handle(viewModelScope, uiBlock) { interactor.tags() }
            handleDeath.handleDeath()
        }
    }

    fun apply(filter: Pair<Long, Boolean>) = runAsync.handle(viewModelScope, uiBlock) {
        interactor.save(filter)
    }

    fun reset() = runAsync.handle(viewModelScope, uiBlock) { interactor.reset() }

    override fun startGettingUpdates(observer: CustomObserver<FilterState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(FilterObserver.Empty)

    override fun clear() = observable.clear()

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}