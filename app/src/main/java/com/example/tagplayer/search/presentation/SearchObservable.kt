package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.search.domain.SearchState

class SearchObservable : CustomObservable.ManualClear<SearchState>(SearchState.Empty)