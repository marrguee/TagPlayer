package com.example.tagplayer.recently.presentation

import android.os.Bundle
import android.view.View
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.R
import com.example.tagplayer.databinding.FragmentRecentlyBinding
import com.example.tagplayer.main.presentation.ComebackFragment
import com.example.tagplayer.tag_settings.presentation.MenuAction

class RecentlyFragment : ComebackFragment<FragmentRecentlyBinding, RecentlyViewModel>() {

    @UnstableApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuOptions = listOf(
            Pair(
                R.id.editSongTagsMenu,
                object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.editSongTagsScreen(args[0] as Long)
                    }
                }
            )
        )
        val adapter = RecentlyListenerAdapter(menuOptions) {
            viewModel.play(it)
        }
        binding.recentlyRecycler.adapter = adapter

        viewModel.init(SaveRestoreRecentlyState(savedInstanceState))
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : RecentlyObserver {
            override fun update(data: RecentlyState) {
                data.dispatch(binding.recentlyRecycler)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(SaveRestoreRecentlyState(outState))
    }
}