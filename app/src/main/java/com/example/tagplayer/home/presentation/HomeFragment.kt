package com.example.tagplayer.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.HomeFragmentScreenBinding
import com.example.tagplayer.playback_control.presentation.PlaybackControlFragment
import com.example.tagplayer.tagsettings.presentation.MenuAction

class HomeFragment : Fragment(R.layout.home_fragment_screen) {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var binding: HomeFragmentScreenBinding
    private lateinit var libraryAdapter: LibraryRecyclerAdapter
    private lateinit var recentlyAdapter: RecentlyRecyclerListenerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentScreenBinding.inflate(inflater)
        return binding.root
    }

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

        libraryAdapter = LibraryRecyclerAdapter(menuOptions) { id ->
            viewModel.play(id)
        }
        recentlyAdapter = RecentlyRecyclerListenerAdapter(menuOptions) { id ->
            viewModel.play(id)
        }
        binding.libraryRecycler.adapter = libraryAdapter
        binding.recentlyRecycler.adapter = recentlyAdapter

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.playbackControlContainer, PlaybackControlFragment())
                .commit()
        }

        binding.recentlyPlayedTextView.setOnClickListener {
            viewModel.recentlyPlayedScreen()
        }

        binding.tagFilterButton.apply {
            setOnLongClickListener {
                viewModel.tagSettingsScreen()
                true
            }
            setOnClickListener {
                viewModel.filterTagsScreen()
            }
        }

        viewModel.scan()
        viewModel.loadRecently()
        viewModel.startGettingFilterUpdates()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : HomeObserver {
            override fun update(data: HomeState) {
                data.dispatch(libraryAdapter, recentlyAdapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates(HomeObserver.Empty)
    }
}

interface HomeObserver : CustomObserver<HomeState> {

    object Empty : HomeObserver {
        override fun update(data: HomeState) = Unit
    }
}