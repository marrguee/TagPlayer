package com.example.tagplayer.tag_settings.add_tag.presentation

import android.os.Bundle
import com.example.tagplayer.core.HandleSaveRestoreState

class SaveAndRestoreTagDialogState(bundle: Bundle?) : HandleSaveRestoreState.Base<TagDialogState>(
    bundle,
    SaveAndRestoreTagDialogState::class.java.name
)