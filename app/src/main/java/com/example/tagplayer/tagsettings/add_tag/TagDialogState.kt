package com.example.tagplayer.tagsettings.add_tag

import android.widget.Button
import android.widget.TextView
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi
import com.google.android.material.textfield.TextInputEditText

interface TagDialogState {
    fun dispatch(dialogTitle: TextView, editText: TextInputEditText, button: Button)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class EditMode(
        private val tagSettingsUi: TagSettingsUi
    ) : TagDialogState {

        override fun dispatch(dialogTitle: TextView, editText: TextInputEditText, button: Button) {
            tagSettingsUi.fillFields(editText)
            dialogTitle.text = dialogTitle.context.resources.getText(R.string.edit_tag_title)
            button.text = button.resources.getText(R.string.edit_tag_button)
        }

    }

    object AddMode : TagDialogState {
        override fun dispatch(dialogTitle: TextView, editText: TextInputEditText, button: Button) {
            dialogTitle.text = dialogTitle.context.resources.getText(R.string.add_tag_title)
            button.text = button.resources.getText(R.string.add_tag_button)
        }
    }

    object Empty : TagDialogState {
        override fun dispatch(dialogTitle: TextView, editText: TextInputEditText, button: Button) =
            Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = Unit
    }
}