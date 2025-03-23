package com.example.tagplayer.tags_attach.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.fragments.ComebackFragment
import com.example.tagplayer.core.presentation.fragments.NewInstance
import com.example.tagplayer.databinding.FragmentAttachTagsBinding

class AttachTagsFragment : ComebackFragment<FragmentAttachTagsBinding, AttachTagsViewModel>(),
    NewInstance<Long, AttachTagsFragment> {
    private lateinit var allAdapter: AttachTagsAdapter
    private lateinit var ownAdapter: AttachTagsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songId = arguments?.getLong(SONG_ID_KEY)?:Long.MIN_VALUE
        if (songId == Long.MIN_VALUE) {
            Toast.makeText(context, R.string.song_id_not_valid, Toast.LENGTH_SHORT).show()
            viewModel.comeback()
        }

        allAdapter = AttachTagsAdapter()
        ownAdapter = AttachTagsAdapter()

        binding.allRecycler.adapter = allAdapter
        binding.ownRecycler.adapter = ownAdapter

        binding.allPlaceholder.setOnDragListener(
            AttachDragListener(binding.allPlaceholder.id) { viewModel.removeFromOwned(it) }
        )

        binding.ownPlaceholder.setOnDragListener(
            AttachDragListener(binding.ownPlaceholder.id) { viewModel.addToOwned(it) }
        )

        viewModel.init(songId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : AttachTagsObserver {
            override fun update(data: AttachTagsState) {
                data.dispatch(
                    requireContext(),
                    allAdapter,
                    ownAdapter,
                    binding.allPlaceholder,
                    binding.ownPlaceholder
                )
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun instance(args: Long): AttachTagsFragment {
        val fragment = AttachTagsFragment()
        val argsBundle = Bundle().apply {
            putLong(SONG_ID_KEY, args)
        }
        fragment.arguments = argsBundle
        return fragment
    }

    companion object {
        private const val SONG_ID_KEY = "SONG_ID_KEY"
    }
}