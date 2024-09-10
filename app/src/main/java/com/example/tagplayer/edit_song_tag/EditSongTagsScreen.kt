package com.example.tagplayer.edit_song_tag

import androidx.fragment.app.FragmentManager
import com.example.tagplayer.main.presentation.Screen

class EditSongTagsScreen(
    private val songId: Long
) : Screen.AddWithBackstack(EditSongTagFragment::class.java) {
    override fun dispatch(fragmentManager: FragmentManager, containerId: Int) {
        fragmentManager.beginTransaction()
            .add(containerId, EditSongTagFragment.newInstance(songId))
            .addToBackStack(EditSongTagFragment::class.java.name)
            .commit()
    }
}