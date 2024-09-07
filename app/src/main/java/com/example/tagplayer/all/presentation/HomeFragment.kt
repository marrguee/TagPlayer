package com.example.tagplayer.all.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.HomeFragmentScreenBinding
import com.example.tagplayer.playback_control.presentation.PlaybackControlFragment

class HomeFragment : Fragment(R.layout.home_fragment_screen) {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var binding: HomeFragmentScreenBinding

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

        val libraryAdapter = LibraryRecyclerAdapter { id ->
            viewModel.play(id)
        }
        val recentlyAdapter = RecentlyRecyclerAdapter { id ->
            viewModel.play(id)
        }
        binding.libraryRecycler.adapter = libraryAdapter
        binding.recentlyRecycler.adapter = recentlyAdapter

        viewModel.observe(this) {
            it.dispatch(libraryAdapter, recentlyAdapter)
        }


        viewModel.loadLibrary()
        viewModel.scan()

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.playbackControlContainer, PlaybackControlFragment())
                .commit()
        }

        binding.recentlyPlayedTextView.setOnClickListener {
            viewModel.recentlyPlayedScreen()
        }

        binding.tagFilterButton.setOnLongClickListener {
            viewModel.tagSettingsScreen()
            true
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRecently()
    }
}