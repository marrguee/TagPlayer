package com.example.tagplayer.edit_song_tags.presentation

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.edit_song_tags.domain.EditSongTagInteractor
import com.example.tagplayer.edit_song_tags.data.EditSongTagsCacheDatasource
import com.example.tagplayer.edit_song_tags.data.EditSongTagsRepositoryImpl
import com.example.tagplayer.main.presentation.Navigation
import java.util.concurrent.atomic.AtomicLong

class EditSongTagModule(
    core: Core,
    private val clear: ClearViewModel,
    private val selectedSongId: AtomicLong
) : Module<EditSongTagsViewModel> {

    private val repository = EditSongTagsRepositoryImpl(
        EditSongTagsCacheDatasource.Base(core.tagDao(), core.songsDao())
    )
    private val interactor = EditSongTagInteractor.Base(repository)
    private val observable = EditSongObservable()

    override fun create(): EditSongTagsViewModel =
        EditSongTagsViewModel(
            interactor,
            Navigation.Base,
            observable,
            clear,
            HandleDeath.Base(),
            selectedSongId
        )
}