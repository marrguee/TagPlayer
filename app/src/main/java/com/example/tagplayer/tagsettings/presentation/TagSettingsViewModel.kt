package com.example.tagplayer.tagsettings.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.tagsettings.domain.TagDomain
import com.example.tagplayer.tagsettings.domain.TagSettingsInteractor

class TagSettingsViewModel(
    private val interactor: TagSettingsInteractor,
    private val communication: Communication<TagSettingsState>,
    private val mapper: TagSettingsResponse.TagSettingsResponseMapper,
    private val clear:() -> Unit
) : ViewModel() {

}