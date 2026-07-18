package com.example.tagplayer.tag_details.presentation.radio_grid

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val radioGridModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<RadioGridObservable> { RadioGridObservable() }

        viewModel {
            RadioGridViewModel(
                observable = get<RadioGridObservable>()
            )
        }
    }
}