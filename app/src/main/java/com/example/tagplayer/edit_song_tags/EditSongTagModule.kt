package com.example.tagplayer.edit_song_tags

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.edit_song_tags.domain.EditSongTagInteractor
import com.example.tagplayer.edit_song_tags.data.EditSongTagsCacheDatasource
import com.example.tagplayer.edit_song_tags.data.EditSongTagsRepositoryImpl
import com.example.tagplayer.edit_song_tags.presentation.EditSongObservable
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsViewModel
import com.example.tagplayer.edit_song_tags.presentation.TagUi
import com.example.tagplayer.main.presentation.Navigation

class EditSongTagModule(
    core: Core,
    private val clear: ClearViewModel
) : Module<EditSongTagsViewModel> {

    private val repository = EditSongTagsRepositoryImpl(
        EditSongTagsCacheDatasource.Base(core.tagDao(), core.songsDao())
    )
    private val interactor = EditSongTagInteractor.Base(repository, TagUi.Mapper.Domain)
    private val observable = EditSongObservable()

    override fun create(): EditSongTagsViewModel =
        EditSongTagsViewModel(
            clear,
            DispatcherList.Base,
            interactor,
            Navigation.Base,
            observable,
            HandleDeath.Base(),
            TagUi.Mapper.Id
        )
}