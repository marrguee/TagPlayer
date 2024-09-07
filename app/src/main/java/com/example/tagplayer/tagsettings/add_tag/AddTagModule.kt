package com.example.tagplayer.tagsettings.add_tag

import androidx.lifecycle.MutableLiveData
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi

class AddTagModule(
    core: Core,
    private val selectedTagLiveData: MutableLiveData<TagSettingsUi?>
) : Module<AddTagViewModel> {
    private val repository: AddTagRepository =
        AddTagRepositoryImpl(AddTagDatasource.Base(core.mediaDatabase().tagDao))
    private val interactor: AddTagInteractor = AddTagInteractor.Base(repository)
    private val communication = Communication.TagDialogCommunication()
    override fun create(): AddTagViewModel = AddTagViewModel(interactor, selectedTagLiveData, communication)
}