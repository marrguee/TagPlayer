package com.example.tagplayer.edit_song_tag

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.EditTagsForSongFragmentBinding

class EditSongTagFragment : Fragment() {
    private lateinit var binding: EditTagsForSongFragmentBinding
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(EditSongTagsViewModel::class.java)
    }
    private lateinit var allTagsAdapter: EditSongTagListenerAdapter
    private lateinit var ownedTagsAdapter: EditSongTagListenerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditTagsForSongFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songId: Long? = arguments?.getLong(SONG_ID_KEY)

        allTagsAdapter = EditSongTagListenerAdapter()
        ownedTagsAdapter = EditSongTagListenerAdapter()


        binding.allTagsRecyclerView.adapter = allTagsAdapter
        binding.ownedTagsRecyclerView.adapter = ownedTagsAdapter

        binding.noAllTagsTextView.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DROP && binding.noAllTagsTextView.id == v.id) {
                val draggedItem: ClipData.Item = event.clipData.getItemAt(0)
                val id: Long = draggedItem.text.toString().toLong()
                viewModel.dragAndDrop(false, id)
            }
            true
        }

        binding.noOwnedTagsTextView.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DROP && binding.noOwnedTagsTextView.id == v.id) {
                val draggedItem: ClipData.Item = event.clipData.getItemAt(0)
                val id: Long = draggedItem.text.toString().toLong()
                viewModel.dragAndDrop(true, id)
            }
            true
        }

        binding.confirmButton.setOnClickListener {
            viewModel.confirm()
        }

        viewModel.init(songId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : EditSongTagObserver {
            override fun update(data: EditSongTagState) {
                data.dispatch(
                    allTagsAdapter,
                    ownedTagsAdapter,
                    binding.noAllTagsTextView,
                    binding.noOwnedTagsTextView
                )
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    companion object {
        fun newInstance(songId: Long): EditSongTagFragment =
            EditSongTagFragment().apply {
                arguments = Bundle().apply {
                    putLong(SONG_ID_KEY, songId)
                }
            }

        private const val SONG_ID_KEY = "SONG_ID_KEY"
    }
}

interface EditSongTagObserver : CustomObserver<EditSongTagState> {
    object Empty : EditSongTagObserver {
        override fun update(data: EditSongTagState) = Unit
    }
}