package com.example.tagplayer.tagsettings.presentation

import android.os.Bundle
import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.databinding.TagsSettingsFragmentScreenBinding
import com.example.tagplayer.main.presentation.ComebackFragment

class TagSettingsFragment : ComebackFragment<TagsSettingsFragmentScreenBinding, TagSettingsViewModel>() {

    private lateinit var adapter: TagsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTagButton.setOnClickListener {
            viewModel.showTagDialog(requireActivity().supportFragmentManager)
        }

        adapter = TagsAdapter(
            listOf(
                R.id.editTagMenu to object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.editTag(args[0] as TagSettingsUi)
                        viewModel.showTagDialog(requireActivity().supportFragmentManager)
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
                data.dispatch(adapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}