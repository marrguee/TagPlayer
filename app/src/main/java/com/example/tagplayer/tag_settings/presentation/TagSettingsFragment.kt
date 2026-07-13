package com.example.tagplayer.tag_settings.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.fragments.ComebackFragment
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.databinding.FragmentTagsSettingsBinding

class TagSettingsFragment : ComebackFragment<FragmentTagsSettingsBinding, TagSettingsViewModel>() {
    private lateinit var adapter: TagsAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { _, _ -> true }

        binding.addTagButton.setOnClickListener {
            viewModel.showTagDialog()
        }

        adapter = TagsAdapter(
            listOf(
                R.id.editTagMenu to object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.showTagDialog(args[0] as Long)
                    }
                },
                R.id.removeTagMenu to object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.deleteTag(args[0] as Long)
                    }
                },
            )
        )
        binding.tagsRecyclerView.adapter = adapter
        registerForContextMenu(binding.tagsRecyclerView)

        viewModel.loadTags()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : TagSettingsObserver {
            override fun update(data: TagSettingsState) {
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