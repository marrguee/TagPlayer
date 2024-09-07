package com.example.tagplayer.tagsettings.add_tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.AddDialogFragmentBinding

class AddTagDialogFragment : DialogFragment() {
    private lateinit var binding: AddDialogFragmentBinding
    private val viewModel: AddTagViewModel by lazy {
        (requireActivity().application as ProvideViewModel).provide(AddTagViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(this) {
            it.dispatch(binding.dialogTitle, binding.tagNameEditText, binding.addTagButton)
        }
        viewModel.init()
        binding.addTagButton.setOnClickListener {
            viewModel.addTag(binding.tagNameEditText.text.toString()) {
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSelected()
    }
}