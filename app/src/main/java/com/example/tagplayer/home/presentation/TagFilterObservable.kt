package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObservable

class TagFilterObservable : CustomObservable.StateHandleManualClear<TagFiltersState>(
    TagFiltersState.Empty
)