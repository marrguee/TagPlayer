package com.example.tagplayer.tags_attach.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable

class AttachTagsObservable :
    CustomObservable.ManualClear<AttachTagsState>(AttachTagsState.Empty)