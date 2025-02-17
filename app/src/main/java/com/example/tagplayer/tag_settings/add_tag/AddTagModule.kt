package com.example.tagplayer.tag_settings.add_tag

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.tag_settings.add_tag.data.AddTagDatasource
import com.example.tagplayer.tag_settings.add_tag.data.AddTagRepositoryImpl
import com.example.tagplayer.tag_settings.add_tag.domain.AddTagInteractor
import com.example.tagplayer.tag_settings.add_tag.domain.AddTagRepository
import com.example.tagplayer.tag_settings.add_tag.presentation.AddTagObservable
import com.example.tagplayer.tag_settings.add_tag.presentation.AddTagViewModel
import com.example.tagplayer.tag_settings.presentation.Selected
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi

class AddTagModule(
    core: Core,
    private val selectedTag: Selected<TagSettingsUi>,
) : Module<AddTagViewModel> {
    private val repository: AddTagRepository =
        AddTagRepositoryImpl(AddTagDatasource.Base(core.mediaDatabase().tagsDao))
    private val interactor: AddTagInteractor = AddTagInteractor.Base(repository)
    private val observable = AddTagObservable()
    override fun create(): AddTagViewModel =
        AddTagViewModel(
            DispatcherList.Base,
            interactor,
            selectedTag,
            observable,
            HandleDeath.Base()
        )
}