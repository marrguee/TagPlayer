package com.example.tagplayer.filter_by_tags.presentation

import android.os.Bundle
import com.example.tagplayer.core.HandleSaveRestoreState

class SaveRestoreFilterState(bundle: Bundle?) :
    HandleSaveRestoreState.Base<FilterScreenState>(
        bundle,
        SaveRestoreFilterState::class.java.name
    )