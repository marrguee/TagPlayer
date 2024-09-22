package com.example.tagplayer.tagsettings.add_tag

import com.example.tagplayer.core.CustomObserver

interface AddTagObserver : CustomObserver<TagDialogState> {
    object Empty : AddTagObserver {
        override fun update(data: TagDialogState) = Unit
    }
}