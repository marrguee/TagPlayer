package com.example.tagplayer.tag_details.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.core.presentation.fragments.NewInstance
import com.example.tagplayer.databinding.FragmentDetailsDialogBinding

class TagDetailsFragment : DialogFragment(), NewInstance<Long, TagDetailsFragment> {
    private lateinit var binding: FragmentDetailsDialogBinding
    private var tagId: Long? = null

    private val viewModel: TagViewModel by lazy {
        (requireActivity().application as ProvideViewModel).provide(
            if (tagId == null) AddTagViewModel::class.java else EditTagViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tagId = arguments?.getLong(TAG_ID_KEY)
        viewModel.init(tagId)

        with(binding) {
            colorGrid.init()

            addTagButton.setOnClickListener {
                val title = tagNameEditText.text?.trim().toString()
                if (title.isNotBlank()) viewModel.accept(title, colorGrid.color()) { dismiss() }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : TagDetailsObserver {
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

    override fun instance(args: Long): TagDetailsFragment = TagDetailsFragment().apply {
        arguments = Bundle().apply { putLong(TAG_ID_KEY, args) }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.colorGrid.comeback()
        viewModel.comeback()
    }

    companion object {
        private const val TAG_ID_KEY = "TAG_ID_KEY"
    }
}