package com.example.tagplayer.edit_song_tag

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.main.presentation.Navigation

class EditSongTagModule(
    private val core: Core
) : Module<EditSongTagsViewModel> {
    private val repository = EditSongTagsRepositoryImpl(
        EditSongTagsCacheDatasource.Base(core.tagDao(), core.songsDao())
    )
    private val interactor = EditSongTagInteractor.Base(repository)
    private val communication = Communication.EditSongTagsCommunication()
    override fun create(): EditSongTagsViewModel {
        return EditSongTagsViewModel(interactor, Navigation.Base, communication)
    }
}