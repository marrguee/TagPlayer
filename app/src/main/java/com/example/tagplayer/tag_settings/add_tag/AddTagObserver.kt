package com.example.tagplayer.tag_settings.add_tag

import com.example.tagplayer.core.CustomObserver

interface AddTagObserver : CustomObserver<TagDialogState> {
    object Empty : AddTagObserver {
        override fun update(data: TagDialogState) = Unit
    }
}