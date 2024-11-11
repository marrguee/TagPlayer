package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObservable

class TagFilterObservable : CustomObservable.AutomaticClear<TagFiltersResponse>(
    TagFiltersResponse.Empty,
    TagFiltersObserver.Empty
)