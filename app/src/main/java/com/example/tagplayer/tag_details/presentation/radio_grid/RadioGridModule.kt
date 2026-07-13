package com.example.tagplayer.tag_details.presentation.radio_grid

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val radioGridModule = module {
    viewModel { RadioGridViewModel(RadioGridObservable()) }
}