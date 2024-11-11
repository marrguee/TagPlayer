package com.example.tagplayer.edit_song_tag.presentation

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.edit_song_tag.domain.EditSongTagInteractor
import com.example.tagplayer.edit_song_tag.data.EditSongTagsCacheDatasource
import com.example.tagplayer.edit_song_tag.data.EditSongTagsRepositoryImpl
import com.example.tagplayer.main.presentation.Navigation

class EditSongTagModule(
    private val core: Core,
    private val clear: ClearViewModel
) : Module<EditSongTagsViewModel> {
    private val repository = EditSongTagsRepositoryImpl(
        EditSongTagsCacheDatasource.Base(core.tagDao(), core.songsDao())
    )
    private val interactor = EditSongTagInteractor.Base(repository)
    private val observable = EditSongObservable()
    override fun create(): EditSongTagsViewModel {
        return EditSongTagsViewModel(interactor, Navigation.Base, observable, clear)
    }
}