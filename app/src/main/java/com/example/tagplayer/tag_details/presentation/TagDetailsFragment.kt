package com.example.tagplayer.tag_details.presentation

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
            val comeback: () -> Unit = {
                colorGrid.comeback()
                viewModel.comeback()
            }

            addTagButton.setOnClickListener {
                val title = tagNameEditText.text?.trim().toString()
                if (title.isNotBlank()) viewModel.accept(title, colorGrid.color(), comeback)
            }

            activity?.onBackPressedDispatcher?.addCallback(
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() = comeback.invoke()
                }
            )
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

    companion object {
        private const val TAG_ID_KEY = "TAG_ID_KEY"
    }
}