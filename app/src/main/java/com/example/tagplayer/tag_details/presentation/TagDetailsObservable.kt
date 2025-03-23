package com.example.tagplayer.tag_details.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable

class TagDetailsObservable :
    CustomObservable.ManualClear<TagDialogState>(TagDialogState.Empty)