package com.example.tagplayer.edit_song_tags.presentation

import com.example.tagplayer.core.CustomObserver

interface EditSongTagObserver : CustomObserver<EditSongTagState> {
    object Empty : EditSongTagObserver {
        override fun update(data: EditSongTagState) = Unit
    }
}