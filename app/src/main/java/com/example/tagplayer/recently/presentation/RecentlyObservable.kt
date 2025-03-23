package com.example.tagplayer.recently.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable

class RecentlyObservable :
    CustomObservable.ManualClear<RecentlyState>(RecentlyState.Empty)