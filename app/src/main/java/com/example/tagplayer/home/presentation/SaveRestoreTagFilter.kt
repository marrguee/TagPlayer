package com.example.tagplayer.home.presentation

import android.os.Bundle
import com.example.tagplayer.core.HandleSaveRestoreState

class SaveRestoreTagFilter(bundle: Bundle?) :
    HandleSaveRestoreState.Base<TagFiltersState>(bundle, SaveRestoreTagFilter::class.java.name)