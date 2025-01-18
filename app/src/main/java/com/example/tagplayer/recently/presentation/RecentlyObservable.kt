package com.example.tagplayer.recently.presentation

import com.example.tagplayer.core.CustomObservable

class RecentlyObservable :
    CustomObservable.StateHandleManualClear<RecentlyState>(RecentlyState.Empty)