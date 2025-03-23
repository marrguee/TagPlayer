package com.example.tagplayer.tags_attach.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface AttachTagsObserver : CustomObserver<AttachTagsState> {
    object Empty : AttachTagsObserver {
        override fun update(data: AttachTagsState) = Unit
    }
}