package com.example.tagplayer.edit_song_tags.presentation

import android.os.Bundle
import com.example.tagplayer.core.HandleSaveRestoreState

class SaveRestoreEditSongTag (bundle: Bundle?) :
    HandleSaveRestoreState.Base<EditSongTagState>(bundle, SaveRestoreEditSongTag::class.java.name)