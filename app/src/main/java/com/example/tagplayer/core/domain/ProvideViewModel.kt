package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagplayer.all.presentation.AllFeatureViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.history.presentation.HistoryViewModel

interface ProvideViewModel {
    fun <T : ViewModel> provide(clazz: Class<out T>) : T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val core: Core,
    ) : ViewModelProvider.Factory {
        private val viewModels: MutableMap<Class<out ViewModel>, ViewModel> = mutableMapOf()
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (viewModels.containsKey(modelClass)) {
                viewModels[modelClass]
            }
            else {
                val viewModel = when (modelClass) {
                    AllFeatureViewModel::class.java -> AllFeatureViewModel(
                        core.runAsync(),
                        core.allInteractor(),
                        core.allCommunication(),
                        core.responseAllMapper()
                    )
                    HistoryViewModel::class.java -> HistoryViewModel(
                        core.historyCommunication(),
                        core.historyInteractor(),
                        core.responseHistoryMapper()
                    )
                    else -> throw IllegalStateException("ViewModel class have not been founded")
                }
                viewModels[modelClass] = viewModel
                viewModel
            } as T
        }
    }
}