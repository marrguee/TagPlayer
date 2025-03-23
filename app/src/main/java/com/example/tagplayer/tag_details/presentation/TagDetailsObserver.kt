package com.example.tagplayer.tag_details.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface TagDetailsObserver : CustomObserver<TagDialogState> {
    object Empty : TagDetailsObserver {
        override fun update(data: TagDialogState) = Unit
    }
}