package com.example.tagplayer.tagsettings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.TagsSettingsFragmentScreenBinding

class TagSettingsFragment : Fragment(R.layout.tags_settings_fragment_screen) {
    private lateinit var binding: TagsSettingsFragmentScreenBinding
    private val viewModel: TagSettingsViewModel by lazy {
        (requireActivity().application as ProvideViewModel).provide(TagSettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TagsSettingsFragmentScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTagButton.setOnClickListener {
            viewModel.showTagDialog(requireActivity().supportFragmentManager)
        }

        val adapter = TagsAdapter(
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

        viewModel.observe(this) {
            it.dispatch(adapter)
        }

        viewModel.loadTags()
    }
}