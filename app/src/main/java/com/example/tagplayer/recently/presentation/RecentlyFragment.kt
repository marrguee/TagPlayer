package com.example.tagplayer.recently.presentation

import android.os.Bundle
import android.view.View
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.R
import com.example.tagplayer.databinding.FragmentRecentlyBinding
import com.example.tagplayer.core.presentation.fragments.ComebackFragment
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction

class RecentlyFragment : ComebackFragment<FragmentRecentlyBinding, RecentlyViewModel>() {
    private lateinit var adapter: RecentlyAdapter

    @UnstableApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuOptions = listOf(
            Pair(
                R.id.editSongTagsMenu,
                object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.attachTagsScreen(args[0] as Long)
                    }
                }
            )
        )
        adapter = RecentlyAdapter(menuOptions) {
            viewModel.play(it)
        }
        binding.recentlyRecycler.adapter = adapter

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : RecentlyObserver {
            override fun update(data: RecentlyState) {
                data.dispatch(requireContext(), adapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}