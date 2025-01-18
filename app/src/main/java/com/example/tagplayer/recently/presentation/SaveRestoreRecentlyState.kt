package com.example.tagplayer.recently.presentation

import android.os.Bundle
import com.example.tagplayer.core.HandleSaveRestoreState

class SaveRestoreRecentlyState(bundle: Bundle?) :
    HandleSaveRestoreState.Base<RecentlyState>(bundle, SaveRestoreRecentlyState::class.java.name)