package com.example.tagplayer.edit_song_tags.presentation

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tagplayer.databinding.EditTagsForSongFragmentBinding
import com.example.tagplayer.main.presentation.ComebackFragment

class EditSongTagFragment : ComebackFragment<EditTagsForSongFragmentBinding, EditSongTagsViewModel>() {
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

        viewModel.init(SaveRestoreEditSongTag(savedInstanceState))
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(SaveRestoreEditSongTag(outState))
    }
}