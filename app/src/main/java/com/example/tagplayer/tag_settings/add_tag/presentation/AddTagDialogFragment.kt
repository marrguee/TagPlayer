package com.example.tagplayer.tag_settings.add_tag.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.FragmentAddDialogBinding

class AddTagDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddDialogBinding
    private val viewModel: AddTagViewModel by lazy {
        (requireActivity().application as ProvideViewModel).provide(AddTagViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            colorGrid.init()

            addTagButton.setOnClickListener {
                viewModel.acceptTag(
                    tagNameEditText.text?.trim().toString(), colorGrid.color()
                ) {
                    dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : AddTagObserver {
            override fun update(data: TagDialogState) {
                with(binding) {
                    data.dispatch(
                        dialogTitle,
                        tagNameEditText,
                        addTagButton,
                        colorGrid
                    )
                }
                data.consumed(viewModel)
            }
        })
        binding.colorGrid.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
        binding.colorGrid.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSelected()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(SaveAndRestoreTagDialogState(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.init(SaveAndRestoreTagDialogState(savedInstanceState))
    }
}