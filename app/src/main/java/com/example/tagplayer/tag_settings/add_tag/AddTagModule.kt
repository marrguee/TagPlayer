package com.example.tagplayer.tag_settings.add_tag

import androidx.lifecycle.MutableLiveData
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi

class AddTagModule(
    core: Core,
    private val selectedTagLiveData: MutableLiveData<TagSettingsUi?>
) : Module<AddTagViewModel> {
    private val repository: AddTagRepository =
        AddTagRepositoryImpl(AddTagDatasource.Base(core.mediaDatabase().tagsDao))
    private val interactor: AddTagInteractor = AddTagInteractor.Base(repository)
    private val observable = AddTagObservable()
    override fun create(): AddTagViewModel =
        AddTagViewModel(interactor, selectedTagLiveData, observable)
}