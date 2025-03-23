package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable

class SearchObservable : CustomObservable.ManualClear<SearchState>(SearchState.Empty)