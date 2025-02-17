package com.example.tagplayer.edit_song_tags.presentation

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import com.example.tagplayer.R
import com.example.tagplayer.databinding.FragmentEditTagsForSongBinding
import com.example.tagplayer.main.presentation.ArgumentsComebackFragment

class EditSongTagFragment
    : ArgumentsComebackFragment<FragmentEditTagsForSongBinding, EditSongTagsViewModel>(
        listOf("SONG_ID")
    ) {
    private lateinit var allTagsAdapter: EditSongTagListenerAdapter
    private lateinit var ownedTagsAdapter: EditSongTagListenerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songId = arguments?.getLong(keyList[0])
        if (songId == null) {
            Toast.makeText(context, R.string.song_id_not_valid, Toast.LENGTH_SHORT).show()
            viewModel.comeback()
        }
        viewModel.consumeId(songId!!)

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